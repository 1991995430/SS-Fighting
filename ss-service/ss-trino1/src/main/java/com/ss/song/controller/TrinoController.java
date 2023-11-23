package com.ss.song.controller;

import com.ss.song.dto.DataDto;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * author shangsong 2023/9/4
 */
@Path("/v1/statement")
public class TrinoController {

    @POST
    @Produces(APPLICATION_JSON)
    public List<DataDto> con1(String statement,
                              @Context HttpServletRequest servletRequest,
                              @Context HttpHeaders httpHeaders)  {
        /*Map<String, String> map = new HashMap<>();
        map.put("value", "value");
        map.put("text", "test");*/
        List<String> list = new ArrayList<>();
        list.add("ssss,111");
        list.add("1111111,222");
        list.add("22222,333");
        List<DataDto> list1 = new ArrayList<>();
        DataDto dataDto = new DataDto();
        dataDto.setText("ssss");
        dataDto.setValue("vcaa");
        DataDto dataDto1 = new DataDto();
        dataDto1.setValue("shang");
        dataDto1.setText("song");
        list1.add(dataDto1);
        list1.add(dataDto);
        return list1;
    }

}
