package com.lzc.othercode;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class DubboTransfer {

    /**
     * 会话超时时间，设置为与系统默认时间一致
     */
    public static final int SESSION_TIMEOUT = 10000;
    /**
     * dubbo 根节点
     */
    public static final String DUBBO_NODE = "/dubbo";
    /**
     * dubbo 备份根节点
     */
    public static final String DUBBO_BACKUP_NODE = "/dubbo_backup";
    /**
     * 服务提供者子节点
     */
    public static final String PROVIDERS_NODE = "/providers";
    
    public static final String CONSUMERS_NODE = "/consumers";
    
    public static final String ROUTES_NODE = "/routers";
    
    public static final String CONFIGURATORS_NODE = "/configurators";
    /**
     * 节点分隔符
     */
    public static final Character SEPARATOR = '/';
    /**
     * URLDecoder 字符集
     */
    private static final String UTF_8 = "UTF-8";
    /**
     * 目标应用列表
     */
    private List<String> targetApplicationList;
    /**
     * ZooKeeper 实例对象
     */
    private ZooKeeper zooKeeper;
    
    /**
     * Zppkeeper node url ip
     */
    private String transIp;
    
    private String transPort;

    /**
     * Dubbo 迁移构造方法
     *
     * @param zookeeperAddress      zookeeper 服务器地址，例：192.168.0.71:2181
     * @param targetApplicationList 目标应用名称，应用在 dubbo 上注册的名称,如：<dubbo:application name="pet_public"/> 就写 pet_public
     * @throws IOException
     */
    public DubboTransfer(String zookeeperAddress, List<String> targetApplicationList) throws IOException {
        this.targetApplicationList = targetApplicationList;

        // 创建 ZooKeeper 实例
        zooKeeper = new ZooKeeper(zookeeperAddress, SESSION_TIMEOUT, new Watcher() {
            public void process(org.apache.zookeeper.WatchedEvent event) {
                System.out.println("event = " + event);
            }
        });
    }
    
    
    public DubboTransfer(String zookeeperAddress, List<String> targetApplicationList,String transIp,String transPort) throws IOException {
        this.targetApplicationList = targetApplicationList;
        this.transIp = transIp;
        this.transPort=transPort;
        // 创建 ZooKeeper 实例
        zooKeeper = new ZooKeeper(zookeeperAddress, SESSION_TIMEOUT, new Watcher() {
            public void process(org.apache.zookeeper.WatchedEvent event) {
                System.out.println("event = " + event);
            }
        });
    }

    /**
     * 获取应用列表
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public List<DubboApplication> getApplicationList() throws InterruptedException, UnsupportedEncodingException, KeeperException {
        return getApplicationList(DUBBO_NODE);
    }

    /**
     * 迁移服务,迁移之前会将当前 dubbo 备份，用于后续的恢复
     *
     * @param sourceTransfer 将要迁移到当前 dubbo 的来源 dubbo
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void transfer(DubboTransfer sourceTransfer) throws KeeperException, UnsupportedEncodingException, InterruptedException {
        try {
            backup();
            clear(DUBBO_NODE, sourceTransfer.getApplicationList());
            push(DUBBO_NODE, sourceTransfer.getApplicationList());

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 恢复 dubbo ,将之前备份的 dubbo 恢复回去
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void recovery() throws InterruptedException, UnsupportedEncodingException, KeeperException {
        try {
            System.out.println("Recovery start ####");
            List<DubboApplication> dubboApplicationList = getApplicationList(DUBBO_BACKUP_NODE);

            if (zooKeeper.exists(DUBBO_NODE, false) == null) {
                create(DUBBO_NODE);
            }
            clear(DUBBO_NODE, dubboApplicationList);
            push(DUBBO_NODE, dubboApplicationList);
            clear(DUBBO_BACKUP_NODE, dubboApplicationList);

            System.out.println("Recovery end ####");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除将当前 dubbo 里 targetApplicationList 中写明的应用
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void clear() throws InterruptedException, UnsupportedEncodingException, KeeperException {
        try {
            clear(getApplicationList());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 删除将当前 dubbo 里 targetApplicationList 中写明应用的备份
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void clearBackup() throws InterruptedException, UnsupportedEncodingException, KeeperException {
        try {
            clear(DUBBO_BACKUP_NODE, getApplicationList(DUBBO_BACKUP_NODE));
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 清理全部备份
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void clearBackupAll() throws InterruptedException, KeeperException {
        try {
            deleteAll(DUBBO_BACKUP_NODE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 备份全部服务 ,备份将当前 dubbo 里 targetApplicationList 中写明的应用
     *
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws KeeperException
     */
    public void backup() throws InterruptedException, UnsupportedEncodingException, KeeperException {
        try {
            System.out.println("Backup start ####");
            List<DubboApplication> dubboApplicationList = getApplicationList();

            if (zooKeeper.exists(DUBBO_BACKUP_NODE, false) == null) {
                create(DUBBO_BACKUP_NODE);
            }

            push(DUBBO_BACKUP_NODE, dubboApplicationList);
            clear(DUBBO_NODE, dubboApplicationList);
            System.out.println("Backup end ####");
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw e;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw e;
        } catch (KeeperException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private List<DubboService> getDubboServiceList2(final String rootNode, DubboApplication dubboApplication) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        List<DubboService> dubboServiceList = new ArrayList<DubboService>();
        if (zooKeeper.exists(rootNode, false) != null) {
            List<String> rootNodeList = zooKeeper.getChildren(rootNode, true);

            for (String interfaceNode : rootNodeList) {
                String path = rootNode + SEPARATOR + interfaceNode + PROVIDERS_NODE;
                if (zooKeeper.exists(path, false) != null) {
                    List<String> nodeList = zooKeeper.getChildren(path, true);
                    List<String> serviceAddressList = new ArrayList<String>();
                    boolean flag = false;
                    for (String dubboAddress : nodeList) {
                        String decodeNode = URLDecoder.decode(dubboAddress, UTF_8);
                        if (decodeNode.contains("application=" + dubboApplication.getApplicationName())) {
                            flag = true;
                            serviceAddressList.add(dubboAddress);
                        }
                    }
                    if (flag) {
                        //consumers
                        List<String> consumersList = new ArrayList<String>();
                        path = rootNode + SEPARATOR + interfaceNode + CONSUMERS_NODE;
                        if (zooKeeper.exists(path, false) != null) {
                            nodeList = zooKeeper.getChildren(path, true);
                            for (String node : nodeList) {
                                consumersList.add(node);
                            }
                        }
                        //routers
                        List<String> routersList = new ArrayList<String>();
                        path = rootNode + SEPARATOR + interfaceNode + ROUTES_NODE;
                        if (zooKeeper.exists(path, false) != null) {
                            nodeList = zooKeeper.getChildren(path, true);
                            for (String node : nodeList) {
                                routersList.add(node);
                            }
                        }

                        //configurators
                        List<String> configuratorsList = new ArrayList<String>();
                        path = rootNode + SEPARATOR + interfaceNode + CONFIGURATORS_NODE;
                        if (zooKeeper.exists(path, false) != null) {
                            nodeList = zooKeeper.getChildren(path, true);
                            for (String node : nodeList) {
                                configuratorsList.add(node);
                            }
                        }

                        DubboService dubboService = new DubboService(interfaceNode, serviceAddressList);
                        dubboService.setConfiguratorsList(configuratorsList);
                        dubboService.setRoutersList(routersList);
                        dubboService.setConsumersList(consumersList);

                        dubboServiceList.add(dubboService);
                    }
                }
            }
        }

        return dubboServiceList;
    }
    
    
	private List<String> getServiceDetail(final String rootNode,
			DubboService dubboService, boolean needProviders,
			boolean needConsumers, boolean needRoutes,
			boolean needConfigurators) throws KeeperException,
			InterruptedException, UnsupportedEncodingException {
		
		Set<String> applicationSet = new HashSet<String>();
		
        if (zooKeeper.exists(rootNode, false) != null && dubboService != null) {

        	String interfaceNode = dubboService.getServiceName();
        	
        	//providers
        	if(needProviders){
	        	List<String> serviceAddressList = new ArrayList<String>();
	            String path = rootNode + SEPARATOR + interfaceNode + PROVIDERS_NODE;
	            if (zooKeeper.exists(path, false) != null) {
	                List<String> nodeList = zooKeeper.getChildren(path, true);
	                for (String node : nodeList) {
	                	System.out.println("transIp:["+transIp+"]--- transPort:["+transPort+"]---->node:["+node+"]");
	                	if(null==transIp||"".equals(transIp)){
		                    serviceAddressList.add(node);
	                	}else{
	                		 String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";  
	                		 node = node.replaceAll(regex, transIp);
	                		 
	                		 if(null!=transPort&&!"".equals(transPort)){
		                		 node =  URLDecoder.decode(node, UTF_8);
	                			 String portRegex="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}:(\\d+))";
		                		 Pattern p=Pattern.compile(portRegex);

		                		 Matcher m=p.matcher(node);
		                		 String port = "";
		                		 while(m.find()){
		                				port = m.group(2);
		                		 }
		                		 node = URLEncoder.encode(node.replace(port, transPort),"UTF-8");
	                		 }
	                		 serviceAddressList.add(node);

	                	}
	                    
	                    String decodeNode = URLDecoder.decode(node, UTF_8);
                        String appName = getApplicationName(decodeNode);
                        if(!applicationSet.contains(appName))
                        	applicationSet.add(appName);
	                }
	
	                dubboService.setServiceAddressList(serviceAddressList);
	            }
        	}
            //consumers
        	if(needConsumers){
	            List<String> consumersList = new ArrayList<String>();
	            String path = rootNode + SEPARATOR + interfaceNode + CONSUMERS_NODE;
	            if (zooKeeper.exists(path, false) != null) {
	            	List<String> nodeList = zooKeeper.getChildren(path, true);
	                for (String node : nodeList) {
	                    consumersList.add(node);
	                }
	                
	                dubboService.setConsumersList(consumersList);
	            }
        	}
            //routers
        	if(needRoutes){
	            List<String> routersList = new ArrayList<String>();
	            String path = rootNode + SEPARATOR + interfaceNode + ROUTES_NODE;
	            if (zooKeeper.exists(path, false) != null) {
	            	List<String> nodeList = zooKeeper.getChildren(path, true);
	                for (String node : nodeList) {
	                    routersList.add(node);
	                }
	                
	                dubboService.setRoutersList(routersList);
	            }
        	}

            //configurators
        	if(needConfigurators){
	            List<String> configuratorsList = new ArrayList<String>();
	            String path = rootNode + SEPARATOR + interfaceNode + CONFIGURATORS_NODE;
	            if (zooKeeper.exists(path, false) != null) {
	            	List<String> nodeList = zooKeeper.getChildren(path, true);
	                for (String node : nodeList) {
	                    configuratorsList.add(node);
	                }
	                
	                dubboService.setConfiguratorsList(configuratorsList);
	            }
        	}

        }
        
        return new ArrayList<String>(applicationSet);
       
    }

    private List<String> getApplication2(final String rootNode) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        Set<String> applicationSet = new HashSet<String>();

        if (zooKeeper.exists(rootNode, false) != null) {
            List<String> rootNodeList = zooKeeper.getChildren(rootNode, false);

            for (String node : rootNodeList) {

                String allNode = rootNode + SEPARATOR + node + PROVIDERS_NODE;

                if (zooKeeper.exists(allNode, false) != null) {
                    List<String> nodeList = zooKeeper.getChildren(allNode, false);
                    for (String n : nodeList) {
                        String decodeNode = URLDecoder.decode(n, UTF_8);
                        String appName = getApplicationName(decodeNode);

                        if (targetApplicationList.contains(appName)) {
                            applicationSet.add(appName);
                        }
                    }
                }
            }
        }
        return new ArrayList<String>(applicationSet);
    }

    private void deleteDubboApplication(final String rootNode, DubboApplication dubboApplication) throws KeeperException, InterruptedException {
        for (DubboService dubboService : dubboApplication.getDubboServices()) {
            deleteAll(rootNode + SEPARATOR + dubboService.getServiceName());
        }
    }

    public void deleteAll(String path) throws KeeperException, InterruptedException {
        try {
            if (zooKeeper.exists(path, false) != null) {
                zooKeeper.delete(path, -1);
                System.out.println("Delete node #### " + path);
            }
        } catch (KeeperException.NotEmptyException e) {
            for (String childrenPath : zooKeeper.getChildren(path, false)) {
                deleteAll(path + "/" + childrenPath);
                deleteAll(path);
            }
        }
    }

    /**
     * 删除指定应用的服务
     *
     * @param dubboApplicationList 应用列表
     * @throws KeeperException
     * @throws InterruptedException
     */
    private void clear(List<DubboApplication> dubboApplicationList) throws KeeperException, InterruptedException {
        clear(DUBBO_NODE, dubboApplicationList);
    }

    private void clear(final String rootNode, List<DubboApplication> dubboApplicationList) throws KeeperException, InterruptedException {
        for (DubboApplication dubboApplication : dubboApplicationList) {
            System.out.println("Clear dubbo application #### " + dubboApplication.getApplicationName());
            deleteDubboApplication(rootNode, dubboApplication);
        }
    }

    private void push(final String rootNode, List<DubboApplication> dubboApplicationList) throws KeeperException, InterruptedException {
        create(rootNode);
        for (DubboApplication dubboApplication : dubboApplicationList) {
            System.out.println("Push dubbo application #### " + dubboApplication.getApplicationName());
            for (DubboService service : dubboApplication.getDubboServices()) {
                create(rootNode + SEPARATOR + service.getServiceName());
                create(rootNode + SEPARATOR + service.getServiceName() + PROVIDERS_NODE);
                for (String address : service.getServiceAddressList()) {
                    create(rootNode + SEPARATOR + service.getServiceName() + PROVIDERS_NODE + SEPARATOR + address);
                }
            }
        }
    }

    public void create(String path) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(path, false) == null) {
            zooKeeper.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("Create node #### " + path);
        }
    }

    private List<DubboApplication> getApplicationList(final String rootNode) throws InterruptedException, UnsupportedEncodingException, KeeperException {

        List<DubboApplication> dubboApplicationList = new ArrayList<DubboApplication>();
        Map<String, DubboApplication> applicationMap = new HashMap<String, DubboApplication>();

        if (zooKeeper.exists(rootNode, false) != null) {
            List<String> rootNodeList = zooKeeper.getChildren(rootNode, true);

            for (String interfaceNode : rootNodeList) {
            	DubboService dubboService = new DubboService(interfaceNode);
            	List<String> appNames = getServiceDetail(rootNode, dubboService, true, true, true, true);
            	for(String appName : appNames){
            		if (!targetApplicationList.contains(appName)) {
            			continue;
            		}
            		
            		DubboApplication dubboApplication = applicationMap.get(appName);
        			if (dubboApplication == null) {
        				dubboApplication = new DubboApplication(appName);
        				List<DubboService> dubboServices = new ArrayList<DubboService>();
        				dubboApplication.setDubboServices(dubboServices);

        				applicationMap.put(appName, dubboApplication);
        				dubboApplicationList.add(dubboApplication);
        			}
        			
        			dubboApplication.getDubboServices().add(dubboService);
            	}
            }
        }
        
        return dubboApplicationList;
    }

    String getApplicationName(String providersNode) {
        Pattern pattern = Pattern.compile("&application=(.+?)&");
        Matcher matcher = pattern.matcher(providersNode);

        if (matcher.find()) {
            String application = matcher.group();
            application = application.replaceAll("&", "");
            application = application.replaceAll("application=", "");
            return application;
        }

        return null;
    }

    private DubboTransfer() {
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
    }

	public String getTransIp() {
		return transIp;
	}

	public void setTransIp(String transIp) {
		this.transIp = transIp;
	}
    
    public String getTransPort() {
		return transPort;
	}


	public void setTransPort(String transPort) {
		this.transPort = transPort;
	}


	public int getCharacterPosition(String source,String patter,int time){
    	//这里是获取"/"符号的位置
        Matcher slashMatcher = Pattern.compile(patter).matcher(source);
        int mIdx = 0;
        while(slashMatcher.find()) {
           mIdx++;
           //当"/"符号第三次出现的位置
           if(mIdx == time){
              break;
           }
        }
        return slashMatcher.start();
    }
	
	
	public static void main(String[] args) {
		List<String> list =new  ArrayList<String>();
		list.add("practice_user");
		
		try {
			DubboTransfer dubboTransfer = new DubboTransfer("127.0.0.1:2181",list,"10.112.1.91","2181");
			DubboTransfer targetTransfer = new DubboTransfer("10.112.1.91:2181", list);
			targetTransfer.transfer(dubboTransfer);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
