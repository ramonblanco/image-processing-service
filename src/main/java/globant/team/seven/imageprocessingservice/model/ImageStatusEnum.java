package globant.team.seven.imageprocessingservice.model;

public enum ImageStatusEnum {

    IN_PROGRESS("In Progress"), COMPLETED("Completed"), FAILED("Failed");

    private String status;

    ImageStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
