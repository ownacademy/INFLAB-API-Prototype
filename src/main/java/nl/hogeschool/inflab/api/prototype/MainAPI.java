/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.hogeschool.inflab.api.prototype;

/**
 *
 * @author ivan
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static spark.Spark.*;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class MainAPI {
    public static void main(String[] args) {
        get("/APIHelp", (request, response) -> {
            return "API calls \n"
                    + "/DockerInfo (Information about Docker machine)\n"
                    + "/DockerImages (Information about the images downloaded on the docker machine)\n"
                    + "/DockerContainers (To see all running containers)\n"
                    + "/DockerAllContainers (To see all containers)\n"
                    + "/DockerRun/<example: ubuntu> (To run a container)\n"
                    + "/DockerRemove/<name or id> (To remove a container)\n"
                    + "/DockerStart/<name or id> (To start a container\n)"
                    + "/DockerStop/<name or id> (To stop a container)";
        });
        
        get("/DockerInfo", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker info"));
        });
        
        get("/DockerImages", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker images"));
        });
        
        get("/DockerContainers", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker ps"));
        });
        
        get("/DockerAllContainers", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker ps -a"));
        });
        
        get("/DockerRun/:name", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker run " + request.params(":name")));
        });
        
        get("/DockerRemove/:name", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker rm " + request.params(":name")));
        });
        
        get("/DockerStart:name", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker start " + request.params(":name")));
        });
        
        get("/DockerStop:name", (request, response) -> {
            Gson gson = new Gson();
            return gson.toJson(RequestToDocker("docker stop " + request.params(":name")));
        });
        
        stop();
    }
    
    private static List RequestToDocker(String command){
        List<String> collectedData = new ArrayList<String>();
        String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            BufferedReader dockerResponse = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = dockerResponse.readLine()) != null) {
                collectedData.add(s);
            }   
                p.waitFor();
            System.out.println ("exit code: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            System.out.println("Exeption message: ");
            System.out.println(e.getMessage());
        }
        return collectedData;
    }
}