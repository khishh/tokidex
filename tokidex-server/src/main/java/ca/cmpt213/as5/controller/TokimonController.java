package ca.cmpt213.as5.controller;

import ca.cmpt213.as5.model.Tokimon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class TokimonController {

    private final AtomicLong atomicLong = new AtomicLong();
    private List<Tokimon> tokimons = new ArrayList<>();
    private final Gson gson = new Gson();

    private final static String JSON_PATH = "data/tokimon.json";

    @GetMapping("/api/tokimon/all")
    public List<Tokimon> getAllTokimons(){
        System.out.println("Endpoint for getting all tokimons");
        return tokimons;
    }

    @GetMapping("/api/tokimon/{tokimonId}")
    public Tokimon getTokimonById(@PathVariable Long tokimonId, HttpServletResponse response){
        System.out.println("EndPoint for gettting one tokimon by id " + tokimonId);
        Tokimon target = findTokimonById(tokimonId);
        if(target != null){
            response.setStatus(200);
        }
        else{
            System.out.println("getTokimonById: No tokimon with that id found");
            response.setStatus(404, "getTokimonById: No tokimon with that id found");
        }
        return target;
    }

    @PostMapping("/api/tokimon/add")
    public void addTokimon(@RequestParam(value = "name") String name,
                           @RequestParam(value = "weight") double weight,
                           @RequestParam(value = "height") double height,
                           @RequestParam(value = "type") String type,
                           @RequestParam(value = "strength") int strength,
                           @RequestParam(value = "color") String color,
                           HttpServletResponse response){
        System.out.println("New Tokimon name == " + name + " " + type + " " + color);
        Tokimon newTokimon = new Tokimon(name, weight, height, type, strength, color);
        newTokimon.setTokimonId(atomicLong.incrementAndGet());
        tokimons.add(newTokimon);
        System.out.println("NewTokimon id == " + newTokimon.getTokimonId());
        response.setStatus(201);
    }

    @PostMapping("/api/tokimon/change/{tokimonId}")
    public void alterTokimonById(@PathVariable Long tokimonId,
                                 @RequestParam(value = "name") String name,
                                 @RequestParam(value = "weight") double weight,
                                 @RequestParam(value = "height") double height,
                                 @RequestParam(value = "type") String type,
                                 @RequestParam(value = "strength") int strength,
                                 @RequestParam(value = "color") String color,
                                 HttpServletResponse response){
        Tokimon target = findTokimonById(tokimonId);
        if(target == null){
            System.out.println("alterTokimonById: No tokimon with that id found");
            response.setStatus(404, "alterTokimonById: No tokimon with that id found");
        }
        else{
            System.out.println(name + " " + weight + " " + height + " " + type + " " + strength + " " + color) ;
            target.setName(name);
            target.setWeight(weight);
            target.setHeight(height);
            target.setType(type);
            target.setStrength(strength);
            target.setColor(color);
            response.setStatus(201);
        }
    }

    @DeleteMapping("/api/tokimon/{tokimonId}")
    public void deleteTokimonById(@PathVariable Long tokimonId, HttpServletResponse response){
        Tokimon target = findTokimonById(tokimonId);
        if(target == null){
            response.setStatus(404, "deleteTokimonById: No tokimon with that id found");
            System.out.println("deleteTokimonById: No tokimon with that id found");
        }
        else{
            tokimons.remove(target);
            response.setStatus(204);
        }
    }

    private Tokimon findTokimonById(long tokimonId){
        Tokimon target = null;
        for(Tokimon t: tokimons){
            if(t.getTokimonId() == tokimonId){
                target = t;
                break;
            }
        }
        return target;
    }

    @PostConstruct
    public void loadTokimonFromJson(){
        System.out.println("loadTokimonFromJson starts");
        try{
            BufferedReader br = new BufferedReader(new FileReader(JSON_PATH));
            Type listType = new TypeToken<ArrayList<Tokimon>>(){}.getType();
            tokimons = gson.fromJson(br, listType);
            System.out.println(tokimons);
            br.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void saveTokimonIntoJson(){
        System.out.println("saveTokimonIntoJson starts");
        String tokimonInJson = gson.toJson(tokimons);
        System.out.println(tokimonInJson);

        try{
            FileWriter fr = new FileWriter(JSON_PATH);
            fr.write(tokimonInJson);
            fr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
