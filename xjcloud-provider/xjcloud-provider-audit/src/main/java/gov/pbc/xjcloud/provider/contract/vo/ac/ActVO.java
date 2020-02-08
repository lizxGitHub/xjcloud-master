//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.vo.ac;

public class ActVO {
    private String taskId;
    private String bizKey;
    private String processDefKey;
    private String currentStepName;
    private String formKey;
    private String title;

    public ActVO() {
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getBizKey() {
        return this.bizKey;
    }

    public String getProcessDefKey() {
        return this.processDefKey;
    }

    public String getCurrentStepName() {
        return this.currentStepName;
    }

    public String getFormKey() {
        return this.formKey;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }

    public void setCurrentStepName(String currentStepName) {
        this.currentStepName = currentStepName;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ActVO)) {
            return false;
        } else {
            ActVO other = (ActVO) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$taskId = this.getTaskId();
                Object other$taskId = other.getTaskId();
                if (this$taskId == null) {
                    if (other$taskId != null) {
                        return false;
                    }
                } else if (!this$taskId.equals(other$taskId)) {
                    return false;
                }

                Object this$bizKey = this.getBizKey();
                Object other$bizKey = other.getBizKey();
                if (this$bizKey == null) {
                    if (other$bizKey != null) {
                        return false;
                    }
                } else if (!this$bizKey.equals(other$bizKey)) {
                    return false;
                }

                Object this$processDefKey = this.getProcessDefKey();
                Object other$processDefKey = other.getProcessDefKey();
                if (this$processDefKey == null) {
                    if (other$processDefKey != null) {
                        return false;
                    }
                } else if (!this$processDefKey.equals(other$processDefKey)) {
                    return false;
                }

                label62:
                {
                    Object this$currentStepName = this.getCurrentStepName();
                    Object other$currentStepName = other.getCurrentStepName();
                    if (this$currentStepName == null) {
                        if (other$currentStepName == null) {
                            break label62;
                        }
                    } else if (this$currentStepName.equals(other$currentStepName)) {
                        break label62;
                    }

                    return false;
                }

                label55:
                {
                    Object this$formKey = this.getFormKey();
                    Object other$formKey = other.getFormKey();
                    if (this$formKey == null) {
                        if (other$formKey == null) {
                            break label55;
                        }
                    } else if (this$formKey.equals(other$formKey)) {
                        break label55;
                    }

                    return false;
                }

                Object this$title = this.getTitle();
                Object other$title = other.getTitle();
                if (this$title == null) {
                    if (other$title != null) {
                        return false;
                    }
                } else if (!this$title.equals(other$title)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ActVO;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $taskId = this.getTaskId();
        result = result * 59 + ($taskId == null ? 43 : $taskId.hashCode());
        Object $bizKey = this.getBizKey();
        result = result * 59 + ($bizKey == null ? 43 : $bizKey.hashCode());
        Object $processDefKey = this.getProcessDefKey();
        result = result * 59 + ($processDefKey == null ? 43 : $processDefKey.hashCode());
        Object $currentStepName = this.getCurrentStepName();
        result = result * 59 + ($currentStepName == null ? 43 : $currentStepName.hashCode());
        Object $formKey = this.getFormKey();
        result = result * 59 + ($formKey == null ? 43 : $formKey.hashCode());
        Object $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        return result;
    }

    public String toString() {
        return "ActVO(taskId=" + this.getTaskId() + ", bizKey=" + this.getBizKey() + ", processDefKey=" + this.getProcessDefKey() + ", currentStepName=" + this.getCurrentStepName() + ", formKey=" + this.getFormKey() + ", title=" + this.getTitle() + ")";
    }
}
