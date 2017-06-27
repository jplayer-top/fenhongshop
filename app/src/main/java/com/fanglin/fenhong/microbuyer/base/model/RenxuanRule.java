package com.fanglin.fenhong.microbuyer.base.model;

import java.util.List;

/**
 * 分红全球购
 * 青岛芳林信息版权所有
 * author:Created by Plucky on 16/8/12-上午10:21.
 * 功能描述: 任选规则
 */
public class RenxuanRule {
    private String rule_id;
    private String rule_name;

    private int goods_nums;//当前选择的数量

    private List<RenxuanRuleDtl> manselect_rules;//细则

    public String getRule_name() {
        return rule_name;
    }

    public int rulesSize() {
        if (manselect_rules == null) return 0;
        return manselect_rules.size();
    }


    public class RenxuanRuleDtl {
        private int index;//当前索引
        private String manselect_money;//任选金额
        private String manselect_nums;//任选数量

        public String getDesc() {
            return "【" + manselect_money + "元任选" + manselect_nums + "件】";
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public double getManselect_money() {
            try {
                return Double.valueOf(manselect_money);
            } catch (Exception e) {
                return 0;
            }

        }

        public int getManselect_nums() {
            try {
                return Integer.valueOf(manselect_nums);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    /**
     * 满任选选择商品时的提示
     *
     * @param currentNum 当前选中商品的数量
     * @return String
     */
    public RuleAndTips getTips(int currentNum) {
        RuleAndTips ruleAndTips = new RuleAndTips();
        RenxuanRuleDtl dtl = getCurRange(currentNum);

        if (dtl != null) {
            int leftNum = dtl.getManselect_nums() - currentNum;
            if (leftNum > 0) {
                if (dtl.index == 0) {
                    ruleAndTips.isHit = false;
                    ruleAndTips.tips = "再购" + leftNum + "件立享" + dtl.getDesc();
                    ruleAndTips.dtl = dtl;
                    return ruleAndTips;
                } else {
                    RenxuanRuleDtl lastDtl = getItem(dtl.index - 1);
                    if (lastDtl != null) {
                        ruleAndTips.isHit = true;
                        ruleAndTips.tips = "已满足" + lastDtl.getDesc();
                        ruleAndTips.dtl = lastDtl;
                        return ruleAndTips;

                    } else {
                        return ruleAndTips;
                    }
                }
            } else {
                ruleAndTips.isHit = true;
                ruleAndTips.tips = "已满足" + dtl.getDesc();
                ruleAndTips.dtl = dtl;
                return ruleAndTips;
            }
        } else {
            return ruleAndTips;
        }
    }

    private RenxuanRuleDtl getItem(int index) {
        if (manselect_rules != null && index < manselect_rules.size() && index >= 0) {
            return manselect_rules.get(index);
        }
        return null;
    }

    private RenxuanRuleDtl getCurRange(int currentNum) {
        if (manselect_rules != null && manselect_rules.size() > 0) {
            int size = manselect_rules.size();
            for (int i = 0; i < size; i++) {
                RenxuanRuleDtl dtl = manselect_rules.get(i);
                if (currentNum <= dtl.getManselect_nums()) {
                    dtl.setIndex(i);
                    return dtl;
                }
            }
            return manselect_rules.get(size - 1);
        } else {
            return null;
        }
    }

    public class RuleAndTips {
        public boolean isHit;//是否处于命中区间
        public RenxuanRuleDtl dtl;
        public String tips;
    }
}
