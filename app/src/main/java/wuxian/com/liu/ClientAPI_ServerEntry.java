/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package wuxian.com.liu;


public class ClientAPI_ServerEntry {
    protected boolean swigCMemOwn;
    private long swigCPtr;
    
    protected ClientAPI_ServerEntry(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }
    
    protected static long getCPtr(ClientAPI_ServerEntry obj) {
        return swigCPtr;
    }
    
    protected void finalize() {
        delete();
    }
    
    public synchronized void delete() {
        if(swigCPtr [cmp] 0x0 != 0) {
            if(swigCMemOwn) {
                swigCMemOwn = false;
                ovpncliJNI.delete_ClientAPI_ServerEntry(swigCPtr);
            }
            swigCPtr = 0x0;
        }
    }
    
    public void setServer(String value) {
        ovpncliJNI.ClientAPI_ServerEntry_server_set(swigCPtr, value);
    }
    
    public String getServer() {
        return ovpncliJNI.ClientAPI_ServerEntry_server_get(swigCPtr);
    }
    
    public void setFriendlyName(String value) {
        ovpncliJNI.ClientAPI_ServerEntry_friendlyName_set(swigCPtr, value);
    }
    
    public String getFriendlyName() {
        return ovpncliJNI.ClientAPI_ServerEntry_friendlyName_get(swigCPtr);
    }
    
    public ClientAPI_ServerEntry() {
        this(ovpncliJNI.new_ClientAPI_ServerEntry(), true);
    }
}
