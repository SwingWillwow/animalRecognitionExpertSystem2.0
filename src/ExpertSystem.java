import java.util.ArrayList;
import java.util.Scanner;

public class ExpertSystem {

    private  boolean run = true;//判断系统是否继续执行的标记
    private ArrayList<Rule> rules;//保存规则集
    private DataBase dataBase;//综合数据库
    public static void main(String[] args) {
        ExpertSystem system = new ExpertSystem();//初始化系统对象
        system.initSystem();//初始化系统
        system.runSystem();//运行系统
        system.closeSystem();//结束系统，并保存规则
    }
    public ExpertSystem(){

    }

    /**
     * 对应课本上的stepForward
     * @return 如果还有规则可用返回这条规则，否则返回null
     */
    private Rule stepForward(){
        for(Rule rule:rules){
            if(dataBase.tryRule(rule))return rule;
        }
        return null;
    }

    /**
     * 等待用户查看结果再继续
     */
    private void waitUserCheck(){
        System.out.println("Press Enter to continue.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    /**
     * 正向推理函数
     */
    private boolean deduce(){
        Rule rule;
        while((rule=stepForward())!=null){
            if(rule.isEndRule()){
                System.out.println("Finish!");
                System.out.println(rule.getConclusions().get(0));
                return true;
            }
        }
        return false;
    }

    /**
     * 初始化系统，从ruleBank.xml中读出规则集
     * 调用dataBase的setPossibleInitFacts来进行初始化
     */
    private void initSystem(){
        rules = new RuleReader().readRules();
        dataBase = new DataBase();
        dataBase.setPossibleInitFacts(rules);
    }

    /**
     * 当用户不退出时，不断循环运行系统
     */
    private void runSystem(){
        while (run){
            showMenu();
            char order = getValidOrder();
            executeByOrder(order);
        }
    }

    /**
     * 根据用户的指令执行系统
     * @param order 用户的指令，a for add, d for delete, s for show, r for run. # for stop
     */
    private void executeByOrder(char order){
        switch (order){
            case 'a':
                addNewRule();
                dataBase.setPossibleInitFacts(rules);
                break;
            case 'd':
                deleteRule();
                dataBase.setPossibleInitFacts(rules);
                break;
            case 's':
                showAllRules();
                break;
            case 'r':
                startDeduce();
                break;
            case '#':
                run=false;
                break;
        }
    }

    /**
     * 选定初始事实，开始正向推理
     */
    private void startDeduce(){
        dataBase.initFacts();
        System.out.println("use rules in following order.");
        if(!deduce()){
            System.out.println("Error! can not finish deduce.");
        }

        waitUserCheck();
    }

    /**
     * 展示菜单
     */
    private void showMenu(){
        System.out.println("Chose want to do next.");
        System.out.println("enter a to add new rule");
        System.out.println("enter d to delete rule");
        System.out.println("enter s to show all rules");
        System.out.println("enter r to run deduce.");
        System.out.println("enter # to end the program.");
    }

    /**
     * 保证获取到一个合法的字符
     * @return 获取到的合法字符
     */
    private char getValidChar(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        char c;
        if(input.length()!=1){
            System.out.println("invalid input.");
            c = getValidChar();
        }
        else {
            c = input.charAt(0);
        }
        return c;
    }

    /**
     * 保证获取到一个合法整数
     * @return 获取到的整数
     */
    private int getValidInt(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        int ret;
        try {
            ret = Integer.parseInt(input);
        }
        catch (Exception e){
            System.out.println("invalid integer.please input again!");
            ret = getValidInt();
        }
        return ret;
    }

    /**
     * 保证获取到一个合法指令
     * @return 获取的指令
     */
    private char getValidOrder(){
        char order = getValidChar();
        if(!isOrderValid(order)){
            return getValidOrder();
        }
        return order;
    }

    /**
     * 保证获取一个合法的布尔值
     * @return true or false
     */
    private boolean getValidBoolean(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();
        if(input.equals("true")||input.equals("false")){
            return Boolean.parseBoolean(input);
        }
        else{
            return getValidBoolean();
        }
    }

    /**
     * 判断指令是否合法
     * @param c 需要判断的指令
     * @return 合法:true 不合法:false
     */
    private boolean isOrderValid(char c){
        return c=='a'||c=='d'||c=='s'||c=='r'||c=='#';
    }

    /**
     * 关闭系统，将规则集写回到ruleBank.xml文件(具体写会的路径在RuleWriter里面定义)
     */
    private void closeSystem(){
        new RuleWriter().writeRules(rules);
    }

    /**
     * 新增一条产生式规则
     */
    private void addNewRule(){
        int id = rules.get(rules.size()-1).getId()+1;
        Rule rule = new Rule();
        rule.setId(id);
        System.out.println("please input conditions(one per line), enter @ to delete least condition if input wrong one. and end with #.");
        rule.setConditions(getNewConditionsOrConclusions());
        System.out.println("please input conclusions(one per line), enter @ to delete least conclusions if input wrong one. and end with #.");
        rule.setConclusions(getNewConditionsOrConclusions());
        System.out.println("is this a end rule? please input true or false");
        rule.setEndRule(getValidBoolean());
        rules.add(rule);
        System.out.println("success.");
    }

    /**
     * 获取一组条件（前件）或是结论（后件）
     * @return 对应的一组前件或是后件
     */
    private ArrayList<String> getNewConditionsOrConclusions(){
        ArrayList<String> list = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while (flag){
            String condition = scanner.nextLine();
            if(condition.length()==1){
                char c=condition.charAt(0);
                switch (c){
                    case '@':
                        if(list.size()!=0){
                            list.remove(list.get(list.size()-1));
                        }
                        break;
                    case '#':
                        flag = false;
                        break;
                }
            }
            else {
                list.add(condition);
            }
        }
        return list;
    }

    /**
     * 删除一条规则
     */
    private void deleteRule(){
        System.out.println("please input the id of the rule to delete.");
        int id = getValidInt();
        for(Rule rule:rules){
            if(rule.getId()==id){
                rules.remove(rule);
                System.out.println("success.");
                return;
            }
        }
        System.out.println("do not have such rule.");
    }
    /**
     * 显示所有规则
     */
    private void showAllRules(){
        for(Rule rule:rules){
            rule.printRule();
        }
    }

}
