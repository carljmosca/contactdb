package com.github.moscaville.contactsdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Classification implements Serializable {

    private Long id;
    private String classificationType;
    private String description;

    public Classification() {
        
    }
    
}
