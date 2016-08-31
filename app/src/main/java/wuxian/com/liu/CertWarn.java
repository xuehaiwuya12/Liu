/**
  * Generated by smali2java 1.0.0.558
  * Copyright (C) 2013 Hensence.com
  */

package net.openvpn.openvpn;

import android.content.DialogInterface;
import android.content.Context;
import java.security.cert.X509Certificate;
import android.app.AlertDialog;
import android.view.View;
import android.util.Log;
import android.os.Handler;
import java.util.Date;
import java.text.DateFormat;
import java.security.cert.CertificateEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.math.BigInteger;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.text.format.DateFormat;
import android.widget.TextView;
import javax.security.auth.x500.X500Principal;
import java.util.HashMap;

abstract class CertWarn implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
    public static final int RESPONSE_ACCEPT = 0x1;
    public static final int RESPONSE_REJECT = 0x0;
    private static final String TAG = "CertWarn";
    
    protected abstract void done(int p1);
    
    
    public CertWarn(Context context, X509Certificate cert, String excep) {
        CertWarn.1 cancel = new Runnable(this) {
            
            public void run() {
                done(0x0);
            }
        };
        try {
            new AlertDialog.Builder(context).setTitle(0x7f0600b2).setView(inflateCertificateView(context, cert, excep)).setPositiveButton(0x7f0600b3, this).setNegativeButton(0x7f0600b4, this).setOnCancelListener(this).create().show();
            return;
        } catch(Exception e) {
            Log.e("CertWarn", "AlertDialog error", e);
            Handler handler = new Handler();
            handler.postDelayed(cancel, 0x0);
        }
    }
    
    public void onClick(DialogInterface dialog, int button_id) {
        // :( Parsing error. Please contact me.
    }
    
    public void onCancel(DialogInterface dialog) {
        done(0x0);
    }
    
    private static final String fingerprint(byte[] bytes) {
        if(bytes == null) {
            return "";
        }
        for(int i = 0x0; i < bytes.length; i = i + 0x1) {
            byte b = bytes[i];
            sb.append(String.format("%02X", Byte.valueOf(b)));
            if((i + 0x1) != bytes.length) {
                sb.append(0x3a);
            }
        }
        return sb.toString();
    }
    
    private static String getDigest(X509Certificate x509Certificate, String algorithm) {
        if(x509Certificate == null) {
            return "";
        }
        try {
            byte[] bytes = getEncoded();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(bytes);
            return fingerprint(digest);
        } catch(CertificateEncodingException ignored) {
            return "";
        } catch(NoSuchAlgorithmException ignored) {
            return "";
        }
    }
    
    private static String getSerialNumber(X509Certificate x509Certificate) {
        if(x509Certificate == null) {
            return "";
        }
        BigInteger serialNumber = getSerialNumber();
        if(serialNumber == null) {
            return "";
        }
        return fingerprint(serialNumber.toByteArray());
    }
    
    private static String formatCertificateDate(Date certificateDate, DateFormat dateFormat) {
        if(certificateDate == null) {
            return "";
        }
        return dateFormat.format(certificateDate);
    }
    
    private static HashMap<String, String> parse_dn(String dn) {
        HashMap<String, String> ret = new HashMap<String, String>();
        StringBuilder[] sb = {();
        localint1,
        ()};
        int sbi = 0x0;
        boolean esc = 0x0;
        for(int i = 0x0; i < length(); i = i + 0x1) {
            char c = charAt(i);
            if((!esc) && (c == 0x5c)) {
                esc = true;
            } else if((!esc) && (c == 0x3d)) {
                sbi = 0x1;
            } else if((!esc) && (c == 0x2c)) {
                if((sb[0x0].length() > 0) && (sb[0x1].length() > 0)) {
                    ret.put(sb[0x0].toString(), sb[0x1].toString());
                    sb[0x0].setLength(0x0);
                    sb[0x1].setLength(0x0);
                }
                sbi = 0x0;
                continue;
            }
            StringBuilder s = sb[sbi];
            if((s.length() > 0) || (c != 0x20)) {
                s.append(c);
            }
            esc = true;
        }
        if((sb[0x0].length() > 0) && (sb[0x1].length() > 0)) {
            ret.put(sb[0x0].toString(), sb[0x1].toString());
        }
        return ret;
    }
    
    private static HashMap<String, String> parse_dn(X500Principal p) {
        return parse_dn(getName("RFC2253"));
    }
    
    private View inflateCertificateView(Context context, X509Certificate cert, String error) {
        LayoutInflater factory = LayoutInflater.from(context);
        View certificateView = factory.inflate(0x7f030004, 0x0);
        DateFormat dateFormat = DateFormat.getDateFormat(context);
        (TextView)certificateView.findViewById(0x7f0a001c).setText(error);
        HashMap<String, String> issuer = parse_dn(cert.getIssuerX500Principal());
        HashMap<String, String> subject = parse_dn(cert.getSubjectX500Principal());
        (TextView)certificateView.findViewById(0x7f0a0020).setText((CharSequence)subject.get("CN"));
        (TextView)certificateView.findViewById(0x7f0a0022).setText((CharSequence)subject.get("O"));
        (TextView)certificateView.findViewById(0x7f0a0024).setText((CharSequence)subject.get("OU"));
        (TextView)certificateView.findViewById(0x7f0a0028).setText((CharSequence)issuer.get("CN"));
        (TextView)certificateView.findViewById(0x7f0a002a).setText((CharSequence)issuer.get("O"));
        (TextView)certificateView.findViewById(0x7f0a002c).setText((CharSequence)issuer.get("OU"));
        (TextView)certificateView.findViewById(0x7f0a0026).setText(getSerialNumber(cert));
        String issuedOn = formatCertificateDate(cert.getNotBefore(), dateFormat);
        (TextView)certificateView.findViewById(0x7f0a002f).setText(issuedOn);
        String expiresOn = formatCertificateDate(cert.getNotAfter(), dateFormat);
        (TextView)certificateView.findViewById(0x7f0a0031).setText(expiresOn);
        (TextView)certificateView.findViewById(0x7f0a0034).setText(getDigest(cert, "SHA256"));
        (TextView)certificateView.findViewById(0x7f0a0036).setText(getDigest(cert, "SHA1"));
        return certificateView;
    }
}
