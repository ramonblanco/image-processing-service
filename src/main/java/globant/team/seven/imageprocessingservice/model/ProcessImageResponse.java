package globant.team.seven.imageprocessingservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProcessImageResponse(String imageId, String status) {

    public ProcessImageResponse(String imageId){
       this(imageId, null );
    }
}
