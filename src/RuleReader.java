import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;

/**
 * 该类用于处理和规则读入相关的操作
 */
public class RuleReader {
    public RuleReader(){

    }

    /**
     * 读入规则
     * @return 一个Rule组成的ArrayList代表了规则集
     */
    public ArrayList<Rule> readRules(){
        String rulePath = "data/ruleBank.xml";//读取规则的路径
        File file = new File(rulePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document=null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        ArrayList<Rule> rules = new ArrayList<>();
        NodeList list = document.getElementsByTagName("rule");
        for(int i=0;i<list.getLength();i++){
            Rule tmpRule = new Rule();
            tmpRule.setConditions(getConditions(list.item(i)));
            tmpRule.setConclusions(getConclusions(list.item(i)));
            tmpRule.setEndRule(isEndRule(list.item(i)));
            tmpRule.setId(getRuleId(list.item(i)));
            rules.add(tmpRule);
        }
        return rules;
    }

    /**
     * 从xml文档中恢复规则的id号
     * @param node 该规则在xml中对应的结点
     * @return 规则的id
     */
    private int getRuleId(Node node){
        String id = node.getAttributes().getNamedItem("id").getNodeValue();
        return Integer.parseInt(id);
    }

    /**
     * 判断规则是不是结束规则（即能不能推出一个结果)
     * @param node 该规则在xml中对应的结点
     * @return 是:true 不是:false
     */
    private boolean isEndRule(Node node){
        String end = node.getAttributes().getNamedItem("end").getNodeValue();
        return Boolean.parseBoolean(end);
    }

    /**
     * 获取规则的前件
     * @param node 该规则在xml中对应的结点
     * @return 该规则的全部前件
     */
    private ArrayList<String> getConditions(Node node){
        ArrayList<String> ret = new ArrayList<>();
        NodeList tmpList = node.getChildNodes();
        for(int i=0;i<tmpList.getLength();i++){
            if(tmpList.item(i) instanceof Element&& tmpList.item(i).getNodeName().equals("conditions")){
                node = tmpList.item(i);
                break;
            }
        }
        NodeList list = node.getChildNodes();
        for(int i=0;i<list.getLength();i++){
            if(list.item(i) instanceof Element){
                ret.add(list.item(i).getTextContent());
            }
        }
        return ret;
    }

    /**
     * 获取规则的后件
     * @param node 该规则在xml中对应的结点
     * @return 该规则的全部后件
     */
    private ArrayList<String> getConclusions(Node node){
        ArrayList<String> ret = new ArrayList<>();
        NodeList tmpList = node.getChildNodes();
        for(int i=0;i<tmpList.getLength();i++){
            if(tmpList.item(i) instanceof Element&& tmpList.item(i).getNodeName().equals("conclusions")){
                node = tmpList.item(i);
                break;
            }
        }
        NodeList list = node.getChildNodes();
        for(int i=0;i<list.getLength();i++){
            if(list.item(i) instanceof Element){
                ret.add(list.item(i).getTextContent());
            }
        }
        return ret;
    }
}
