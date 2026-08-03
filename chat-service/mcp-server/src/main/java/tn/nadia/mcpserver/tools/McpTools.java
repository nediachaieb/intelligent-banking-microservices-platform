package tn.nadia.mcpserver.tools;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class McpTools {
    @McpTool(name = "getEmployee", description = "Get employee information by name")

    public Employee getEmployee(@McpArg(description = "the employee name") String name) {
        return new Employee(name, 50000.0, 5);

    }
    @McpTool(name = "getAllEmployees", description = "Get a list of employees")
    public List<Employee> getEmployees() {
        return List.of(
                new Employee("Alice", 60000.0, 3),
                new Employee("Bob", 70000.0, 4),
                new Employee("Charlie", 50000.0, 2)
        );
    }



}

record Employee(String name, double salary, int seniority) {}