package CapstoneProject.Capstoneproject1.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseDto {
    private String result;
    private Object data;

    public ResponseDto(String result) {
        this.result = result;
    }

    public ResponseDto(String result, Object data) {
        this.result = result;
        this.data = data;
    }
}
