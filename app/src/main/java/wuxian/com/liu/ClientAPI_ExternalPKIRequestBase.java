/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package net.openvpn.openvpn;


public class ClientAPI_ExternalPKIRequestBase {
    protected boolean swigCMemOwn;
    private long swigCPtr;
    
    protected ClientAPI_ExternalPKIRequestBase(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }
    
    protected static long getCPtr(ClientAPI_ExternalPKIRequestBase obj) {
        return swigCPtr;
    }
    
    protected void finalize() {
        delete();
    }
    
    public synchronized void delete() {
        if(swigCPtr [cmp] 0x0 != 0) {
            if(swigCMemOwn) {
                swigCMemOwn = false;
                ovpncliJNI.delete_ClientAPI_ExternalPKIRequestBase(swigCPtr);
            }
            swigCPtr = 0x0;
        }
    }
    
    public ClientAPI_ExternalPKIRequestBase() {
        this(ovpncliJNI.new_ClientAPI_ExternalPKIRequestBase(), true);
    }
    
    public void setError(boolean value) {
        ovpncliJNI.ClientAPI_ExternalPKIRequestBase_error_set(swigCPtr, value);
    }
    
    public boolean getError() {
        return ovpncliJNI.ClientAPI_ExternalPKIRequestBase_error_get(swigCPtr);
    }
    
    public void setErrorText(String value) {
        ovpncliJNI.ClientAPI_ExternalPKIRequestBase_errorText_set(swigCPtr, value);
    }
    
    public String getErrorText() {
        return ovpncliJNI.ClientAPI_ExternalPKIRequestBase_errorText_get(swigCPtr);
    }
    
    public void setInvalidAlias(boolean value) {
        ovpncliJNI.ClientAPI_ExternalPKIRequestBase_invalidAlias_set(swigCPtr, value);
    }
    
    public boolean getInvalidAlias() {
        return ovpncliJNI.ClientAPI_ExternalPKIRequestBase_invalidAlias_get(swigCPtr);
    }
    
    public void setAlias(String value) {
        ovpncliJNI.ClientAPI_ExternalPKIRequestBase_alias_set(swigCPtr, value);
    }
    
    public String getAlias() {
        return ovpncliJNI.ClientAPI_ExternalPKIRequestBase_alias_get(swigCPtr);
    }
}
