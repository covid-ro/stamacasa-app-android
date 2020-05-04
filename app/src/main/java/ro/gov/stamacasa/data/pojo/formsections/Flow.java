package ro.gov.stamacasa.data.pojo.formsections;

import java.util.List;

public class Flow {

    private String flow_id;
    private String flow_name;

    private List<FlowSection> flow_sections;

    public String getFlow_id() {
        return flow_id;
    }

    public String getFlow_name() {
        return flow_name;
    }

    public List<FlowSection> getFlow_sections() {
        return flow_sections;
    }


}
