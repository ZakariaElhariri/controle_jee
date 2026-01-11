package net.youssfi.mcpserver.tools;


import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class McpTools {

    //on le rend un tool
    @McpTool(name="getEmployee",
            // la ou on choisit la langue
            description = "Get information about a given employee")
// in description, we explain to the agent what the tool does
    // when i change this description it doesn't work
    public Employee getEmployee(@McpArg(description="The employee name") String name){
        return new Employee(name,12300, 4);
    }

    //this tool will get all employees
    @McpTool(description = "Get All Employees")
    public List<Employee> getAllEmployees(){
        return List.of(
                //list d'employes
                new Employee("Hassan",12300,4),
                new Employee("Mohamed",34000,1),
                new Employee("Imane",23000,10)
        );
    }

}

record Employee(String name, double salary, int seniority){

}

