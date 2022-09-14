package br.com.elo.challenge.planetexplorer.dtos.output;

import br.com.elo.challenge.planetexplorer.enums.RegisterType;
import br.com.elo.challenge.planetexplorer.models.SpaceRegister;
import com.fasterxml.jackson.annotation.JsonAlias;

public class RegisterWithMessage {
    String message;
    RegisterType registerType;
    SpaceRegister register;

    //TODO - > Colocar type do register e mudar o campo para values

    public RegisterWithMessage() {
    }

    public RegisterWithMessage(String message, RegisterType registerType, SpaceRegister register) {
        this.message = message;
        this.registerType = registerType;
        this.register = register;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SpaceRegister getRegister() {
        return register;
    }

    public void setRegister(SpaceRegister register) {
        this.register = register;
    }

    public RegisterType getRegisterType() {
        return registerType;
    }

    public void setRegisterType(RegisterType registerType) {
        this.registerType = registerType;
    }
}
