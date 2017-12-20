import java.util.ArrayList;

/**
 * 该类定义了规则的结构
 */
public class Rule {
    private int id;//规则的id
    private ArrayList<String> conditions = new ArrayList<>();//规则的全部前件
    private ArrayList<String> conclusions = new ArrayList<>();//规则的全部后件
    private boolean endRule;//是否结束规则的标记
    /*
        getters and setters
     */
    public ArrayList<String> getConditions() {
        return conditions;
    }

    public void setConditions(ArrayList<String> conditions) {
        this.conditions = conditions;
    }

    public ArrayList<String> getConclusions() {
        return conclusions;
    }

    public void setConclusions(ArrayList<String> conclusions) {
        this.conclusions = conclusions;
    }

    public boolean isEndRule() {
        return endRule;
    }

    public void setEndRule(boolean endRule) {
        this.endRule = endRule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 打印这条规则
     */
    public void printRule(){
        System.out.print("No."+getId()+" rule:");
        System.out.print("IF ");
        ArrayList<String> conditions = getConditions();
        ArrayList<String> conclusions = getConclusions();
        System.out.print(conditions.get(0));
        for(int i=1;i<conditions.size();i++){
            System.out.print(" AND "+conditions.get(i));
        }
        System.out.print(" THEN ");
        System.out.print(conclusions.get(0));
        for(int i=1;i<conclusions.size();i++){
            System.out.print(" AND "+conclusions.get(i));
        }
        System.out.println();
    }
}
