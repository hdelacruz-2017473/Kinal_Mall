
package org.humbertodelacruz.bean;


public class Login {
    
    private String usuarioMaster;
    private String passwordLogin;

    public Login() {
    }

    public Login(String usuarioMaster, String password) {
        this.usuarioMaster = usuarioMaster;
        this.passwordLogin = password;
    }

    public String getUsuarioMaster() {
        return usuarioMaster;
    }

    public void setUsuarioMaster(String usuarioMaster) {
        this.usuarioMaster = usuarioMaster;
    }

    public String getPassword() {
        return passwordLogin;
    }

    public void setPassword(String password) {
        this.passwordLogin = password;
    }
    
    
}
