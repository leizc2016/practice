package com.lzc.othercode;

import java.io.Serializable;
import java.util.List;

public class DubboApplication implements Serializable{
    private static final long serialVersionUID = -8508041176948370663L;

    private String applicationName;
    private List<DubboService> dubboServices;

    public DubboApplication(String appName) {
        this.applicationName = appName;

    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<DubboService> getDubboServices() {
        return dubboServices;
    }

    public void setDubboServices(List<DubboService> dubboServices) {
        this.dubboServices = dubboServices;
    }
}
