package br.com.elo.challenge.planetexplorer.dtos.output;

public class ExplorerWithMessage {
    String message;
    ExplorerInDetails explorer;

    public ExplorerWithMessage() {
    }

    public ExplorerWithMessage(String message, ExplorerInDetails explorer) {
        this.message = message;
        this.explorer = explorer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ExplorerInDetails getExplorer() {
        return explorer;
    }

    public void setExplorer(ExplorerInDetails explorer) {
        this.explorer = explorer;
    }
}
