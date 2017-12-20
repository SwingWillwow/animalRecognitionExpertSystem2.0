import java.util.ArrayList;
import java.util.Scanner;

/**
 * 综合数据库，包含了推理中已知的事实。
 */
public class DataBase {
    private ArrayList<String> facts = new ArrayList<>();//所有已知的事实
    private ArrayList<String> possibleInitFacts = new ArrayList<>();//可以作为初始事实的部分事实
    public DataBase(){

    }



    /**
     * 根据规则集初始化可能的事实集合
     * @param rules 所有规则集
     */
    public void setPossibleInitFacts(ArrayList<Rule> rules){
        ArrayList<String> allFacts = new ArrayList<>();
        for(Rule rule:rules){
            ArrayList<String> conditions = rule.getConditions();
            for(String condition: conditions){
                if(!allFacts.contains(condition)){
                    allFacts.add(condition);
                }
            }
        }
        for(Rule rule:rules){
            ArrayList<String> conclusions = rule.getConclusions();
            for(String conclusion:conclusions){
                if(allFacts.contains(conclusion)){
                    allFacts.remove(conclusion);
                }
            }
        }
        possibleInitFacts = allFacts;

    }
    /*
        getters and setters
     */
    public ArrayList<String> getFacts() {
        return facts;
    }

    public ArrayList<String> getPossibleInitFacts() {
        return possibleInitFacts;
    }

    public void setFacts(ArrayList<String> facts) {
        this.facts = facts;
    }

    public void addFacts(ArrayList<String> newFacts){
        facts.addAll(newFacts);
    }

    /**
     * 对应课本上的try rule,判断一条规则能否使用和用没用过
     * @param rule 用于判断的规则
     * @return true: 可以使用并使用 false: 不能使用
     */
    public boolean tryRule(Rule rule){
        if(testRule(rule)){
            if(useThen(rule)){
                rule.printRule();
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * 对应课本上的useThen 使用一条规则，如果该规则的后件都已经在事实表里面，返回false
     * @param rule 这条规则
     * @return true: 使用了这条规则 false:这条规则中的后件已经全部在事实库中
     */
    private boolean useThen(Rule rule){
        ArrayList<String> conclusions = rule.getConclusions();
        boolean flag = false;
        for(String conclusion:conclusions){
            if(remember(conclusion)){
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 对应书本上的remember 判断一个事实在不在事实库里面如果不在就加入并返回true 否则返回false
     * @param fact 一个事实
     * @return true: 事实库中还没有，并将其加入 false: 已存在
     */
    private boolean remember(String fact){
        if(facts.contains(fact)){
            return false;
        }
        else {
            facts.add(fact);
            return true;
        }
    }

    /**
     * 判断一条规则的前件是否全部符合事实
     * @param rule 这条规则
     * @return true: 全部符合 false:有部分不符合
     */
    private boolean testRule(Rule rule){
        ArrayList<String> conditions = rule.getConditions();
        for (String condition:conditions){
            if(!recallSingleFact(condition)) return false;
        }
        return true;
    }

    /**
     * 对应书上的recall
     * @param fact 一个事实
     * @return 判断这个事实在不在我们的实时库中
     */
    private boolean recallSingleFact(String fact){
        return facts.contains(fact);
    }

    /**
     * 初始化事实表
     */
    public void initFacts(){
        facts.clear();
        ArrayList<Integer> numbers = new ArrayList<>();
        System.out.println("those are possible facts. please choice them with number(one per line).");
        System.out.println("end with a sharp(#)");
        for(String fact:possibleInitFacts){
            System.out.println("NO."+(possibleInitFacts.indexOf(fact)+1)+": "+fact);
        }
        Scanner scanner = new Scanner(System.in);
        while (true){
            String input = scanner.next();
            if(input.length()==1&&input.charAt(0)=='#'){
                break;
            }
            if(!isInteger(input)) continue;
            if(numbers.contains(Integer.parseInt(input))){
                System.out.println("duplicated number.");
                continue;
            }
            numbers.add(Integer.parseInt(input));
        }
        for(Integer num:numbers){
            facts.add(possibleInitFacts.get(num-1));
        }
        System.out.println("init complete. start deduce.");
    }

    private boolean isInteger(String input){
        try {
            int id = Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("invalid input! please try again!");
            return false;
        }
        return true;
    }



}
