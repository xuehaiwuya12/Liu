/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package net.openvpn.openvpn;


public class ClientAPI_OpenVPNClient extends ClientAPI_TunBuilderBase {
    private long swigCPtr;
    
    protected ClientAPI_OpenVPNClient(long cPtr, boolean cMemoryOwn) {
        // :( Parsing error. Please contact me.
    }
    
    protected static long getCPtr(ClientAPI_OpenVPNClient obj) {
        return swigCPtr;
    }
    
    protected void finalize() {
        delete();
    }
    
    public synchronized void delete() {
        if(swigCPtr [cmp] 0x0 != 0) {
            if(swigCMemOwn) {
                swigCMemOwn = false;
                ovpncliJNI.delete_ClientAPI_OpenVPNClient(swigCPtr);
            }
            swigCPtr = 0x0;
        }
        super.delete();
    }
    
    protected void swigDirectorDisconnect() {
        swigCMemOwn = false;
        delete();
    }
    
    public void swigReleaseOwnership() {
        swigCMemOwn = false;
        ovpncliJNI.ClientAPI_OpenVPNClient_change_ownership(this, swigCPtr);
    }
    
    public void swigTakeOwnership() {
        swigCMemOwn = true;
        ovpncliJNI.ClientAPI_OpenVPNClient_change_ownership(this, swigCPtr);
    }
    
    public ClientAPI_OpenVPNClient() {
        this(ovpncliJNI.new_ClientAPI_OpenVPNClient(), true);
        ovpncliJNI.ClientAPI_OpenVPNClient_director_connect(this, swigCPtr, true);
    }
    
    public static void init_process() {
        ovpncliJNI.ClientAPI_OpenVPNClient_init_process();
    }
    
    public static void uninit_process() {
        ovpncliJNI.ClientAPI_OpenVPNClient_uninit_process();
    }
    
    public static ClientAPI_MergeConfig merge_config_static(String path, boolean follow_references) {
        return new ClientAPI_MergeConfig(ovpncliJNI.ClientAPI_OpenVPNClient_merge_config_static(path, follow_references), true);
    }
    
    public static ClientAPI_MergeConfig merge_config_string_static(String config_content) {
        return new ClientAPI_MergeConfig(ovpncliJNI.ClientAPI_OpenVPNClient_merge_config_string_static(config_content), true);
    }
    
    public static ClientAPI_EvalConfig eval_config_static(ClientAPI_Config config) {
        return new ClientAPI_EvalConfig(ovpncliJNI.ClientAPI_OpenVPNClient_eval_config_static(ClientAPI_Config.getCPtr(config)), true);
    }
    
    public static int max_profile_size() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_max_profile_size();
    }
    
    public static boolean parse_dynamic_challenge(String cookie, ClientAPI_DynamicChallenge dc) {
        return ovpncliJNI.ClientAPI_OpenVPNClient_parse_dynamic_challenge(cookie, ClientAPI_DynamicChallenge.getCPtr(dc));
    }
    
    public ClientAPI_EvalConfig eval_config(ClientAPI_Config arg0) {
        return new ClientAPI_EvalConfig(ovpncliJNI.ClientAPI_OpenVPNClient_eval_config(swigCPtr, this, ClientAPI_Config.getCPtr(arg0), arg0), true);
    }
    
    public ClientAPI_Status provide_creds(ClientAPI_ProvideCreds arg0) {
        return new ClientAPI_Status(ovpncliJNI.ClientAPI_OpenVPNClient_provide_creds(swigCPtr, this, ClientAPI_ProvideCreds.getCPtr(arg0), arg0), true);
    }
    
    public boolean socket_protect(int socket) {
        return ovpncliJNI.ClientAPI_OpenVPNClient_socket_protect(swigCPtr, socket);
    }
    
    public ClientAPI_Status connect() {
        return new ClientAPI_Status(ovpncliJNI.ClientAPI_OpenVPNClient_connect(swigCPtr), true);
    }
    
    public ClientAPI_ConnectionInfo connection_info() {
        return new ClientAPI_ConnectionInfo(ovpncliJNI.ClientAPI_OpenVPNClient_connection_info(swigCPtr), true);
    }
    
    public boolean session_token(ClientAPI_SessionToken tok) {
        return ovpncliJNI.ClientAPI_OpenVPNClient_session_token(swigCPtr, this, ClientAPI_SessionToken.getCPtr(tok), tok);
    }
    
    public void stop() {
        ovpncliJNI.ClientAPI_OpenVPNClient_stop(swigCPtr);
    }
    
    public void pause(String reason) {
        ovpncliJNI.ClientAPI_OpenVPNClient_pause(swigCPtr, reason);
    }
    
    public void resume() {
        ovpncliJNI.ClientAPI_OpenVPNClient_resume(swigCPtr);
    }
    
    public void reconnect(int seconds) {
        ovpncliJNI.ClientAPI_OpenVPNClient_reconnect(swigCPtr, seconds);
    }
    
    public boolean pause_on_connection_timeout() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_pause_on_connection_timeout(swigCPtr);
    }
    
    public static int stats_n() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_stats_n();
    }
    
    public static String stats_name(int index) {
        return ovpncliJNI.ClientAPI_OpenVPNClient_stats_name(index);
    }
    
    public long stats_value(int index) {
        return ovpncliJNI.ClientAPI_OpenVPNClient_stats_value(swigCPtr, index);
    }
    
    public ClientAPI_LLVector stats_bundle() {
        return new ClientAPI_LLVector(ovpncliJNI.ClientAPI_OpenVPNClient_stats_bundle(swigCPtr), true);
    }
    
    public ClientAPI_InterfaceStats tun_stats() {
        return new ClientAPI_InterfaceStats(ovpncliJNI.ClientAPI_OpenVPNClient_tun_stats(swigCPtr), true);
    }
    
    public ClientAPI_TransportStats transport_stats() {
        return new ClientAPI_TransportStats(ovpncliJNI.ClientAPI_OpenVPNClient_transport_stats(swigCPtr), true);
    }
    
    public void event(ClientAPI_Event arg0) {
        ovpncliJNI.ClientAPI_OpenVPNClient_event(swigCPtr, this, ClientAPI_Event.getCPtr(arg0), arg0);
    }
    
    public void log(ClientAPI_LogInfo arg0) {
        ovpncliJNI.ClientAPI_OpenVPNClient_log(swigCPtr, this, ClientAPI_LogInfo.getCPtr(arg0), arg0);
    }
    
    public void external_pki_cert_request(ClientAPI_ExternalPKICertRequest arg0) {
        ovpncliJNI.ClientAPI_OpenVPNClient_external_pki_cert_request(swigCPtr, this, ClientAPI_ExternalPKICertRequest.getCPtr(arg0), arg0);
    }
    
    public void external_pki_sign_request(ClientAPI_ExternalPKISignRequest arg0) {
        ovpncliJNI.ClientAPI_OpenVPNClient_external_pki_sign_request(swigCPtr, this, ClientAPI_ExternalPKISignRequest.getCPtr(arg0), arg0);
    }
    
    public static String crypto_self_test() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_crypto_self_test();
    }
    
    public static int app_expire() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_app_expire();
    }
    
    public static String platform() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_platform();
    }
    
    public static String copyright() {
        return ovpncliJNI.ClientAPI_OpenVPNClient_copyright();
    }
}
