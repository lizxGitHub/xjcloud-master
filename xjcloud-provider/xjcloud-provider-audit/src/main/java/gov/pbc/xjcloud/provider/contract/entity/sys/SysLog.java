//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package gov.pbc.xjcloud.provider.contract.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;

public class SysLog implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
        value = "id",
        type = IdType.AUTO
    )
    private Long id;
    @NotBlank(
        message = "日志类型不能为空"
    )
    private String type;
    @NotBlank(
        message = "日志标题不能为空"
    )
    private String title;
    private String createBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String remoteAddr;
    private String userAgent;
    private String requestUri;
    private String method;
    private String params;
    private Long time;
    private String exception;
    private String serviceId;
    @TableLogic
    private String delFlag;

    public SysLog() {
    }

    public Long getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    public String getRemoteAddr() {
        return this.remoteAddr;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public String getRequestUri() {
        return this.requestUri;
    }

    public String getMethod() {
        return this.method;
    }

    public String getParams() {
        return this.params;
    }

    public Long getTime() {
        return this.time;
    }

    public String getException() {
        return this.exception;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SysLog)) {
            return false;
        } else {
            SysLog other = (SysLog)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label191: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label191;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label191;
                    }

                    return false;
                }

                Object this$type = this.getType();
                Object other$type = other.getType();
                if (this$type == null) {
                    if (other$type != null) {
                        return false;
                    }
                } else if (!this$type.equals(other$type)) {
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

                label170: {
                    Object this$createBy = this.getCreateBy();
                    Object other$createBy = other.getCreateBy();
                    if (this$createBy == null) {
                        if (other$createBy == null) {
                            break label170;
                        }
                    } else if (this$createBy.equals(other$createBy)) {
                        break label170;
                    }

                    return false;
                }

                label163: {
                    Object this$createTime = this.getCreateTime();
                    Object other$createTime = other.getCreateTime();
                    if (this$createTime == null) {
                        if (other$createTime == null) {
                            break label163;
                        }
                    } else if (this$createTime.equals(other$createTime)) {
                        break label163;
                    }

                    return false;
                }

                Object this$updateTime = this.getUpdateTime();
                Object other$updateTime = other.getUpdateTime();
                if (this$updateTime == null) {
                    if (other$updateTime != null) {
                        return false;
                    }
                } else if (!this$updateTime.equals(other$updateTime)) {
                    return false;
                }

                Object this$remoteAddr = this.getRemoteAddr();
                Object other$remoteAddr = other.getRemoteAddr();
                if (this$remoteAddr == null) {
                    if (other$remoteAddr != null) {
                        return false;
                    }
                } else if (!this$remoteAddr.equals(other$remoteAddr)) {
                    return false;
                }

                label142: {
                    Object this$userAgent = this.getUserAgent();
                    Object other$userAgent = other.getUserAgent();
                    if (this$userAgent == null) {
                        if (other$userAgent == null) {
                            break label142;
                        }
                    } else if (this$userAgent.equals(other$userAgent)) {
                        break label142;
                    }

                    return false;
                }

                label135: {
                    Object this$requestUri = this.getRequestUri();
                    Object other$requestUri = other.getRequestUri();
                    if (this$requestUri == null) {
                        if (other$requestUri == null) {
                            break label135;
                        }
                    } else if (this$requestUri.equals(other$requestUri)) {
                        break label135;
                    }

                    return false;
                }

                Object this$method = this.getMethod();
                Object other$method = other.getMethod();
                if (this$method == null) {
                    if (other$method != null) {
                        return false;
                    }
                } else if (!this$method.equals(other$method)) {
                    return false;
                }

                label121: {
                    Object this$params = this.getParams();
                    Object other$params = other.getParams();
                    if (this$params == null) {
                        if (other$params == null) {
                            break label121;
                        }
                    } else if (this$params.equals(other$params)) {
                        break label121;
                    }

                    return false;
                }

                Object this$time = this.getTime();
                Object other$time = other.getTime();
                if (this$time == null) {
                    if (other$time != null) {
                        return false;
                    }
                } else if (!this$time.equals(other$time)) {
                    return false;
                }

                label107: {
                    Object this$exception = this.getException();
                    Object other$exception = other.getException();
                    if (this$exception == null) {
                        if (other$exception == null) {
                            break label107;
                        }
                    } else if (this$exception.equals(other$exception)) {
                        break label107;
                    }

                    return false;
                }

                Object this$serviceId = this.getServiceId();
                Object other$serviceId = other.getServiceId();
                if (this$serviceId == null) {
                    if (other$serviceId != null) {
                        return false;
                    }
                } else if (!this$serviceId.equals(other$serviceId)) {
                    return false;
                }

                Object this$delFlag = this.getDelFlag();
                Object other$delFlag = other.getDelFlag();
                if (this$delFlag == null) {
                    if (other$delFlag != null) {
                        return false;
                    }
                } else if (!this$delFlag.equals(other$delFlag)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SysLog;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $type = this.getType();
        result = result * 59 + ($type == null ? 43 : $type.hashCode());
        Object $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        Object $createBy = this.getCreateBy();
        result = result * 59 + ($createBy == null ? 43 : $createBy.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : $createTime.hashCode());
        Object $updateTime = this.getUpdateTime();
        result = result * 59 + ($updateTime == null ? 43 : $updateTime.hashCode());
        Object $remoteAddr = this.getRemoteAddr();
        result = result * 59 + ($remoteAddr == null ? 43 : $remoteAddr.hashCode());
        Object $userAgent = this.getUserAgent();
        result = result * 59 + ($userAgent == null ? 43 : $userAgent.hashCode());
        Object $requestUri = this.getRequestUri();
        result = result * 59 + ($requestUri == null ? 43 : $requestUri.hashCode());
        Object $method = this.getMethod();
        result = result * 59 + ($method == null ? 43 : $method.hashCode());
        Object $params = this.getParams();
        result = result * 59 + ($params == null ? 43 : $params.hashCode());
        Object $time = this.getTime();
        result = result * 59 + ($time == null ? 43 : $time.hashCode());
        Object $exception = this.getException();
        result = result * 59 + ($exception == null ? 43 : $exception.hashCode());
        Object $serviceId = this.getServiceId();
        result = result * 59 + ($serviceId == null ? 43 : $serviceId.hashCode());
        Object $delFlag = this.getDelFlag();
        result = result * 59 + ($delFlag == null ? 43 : $delFlag.hashCode());
        return result;
    }

    public String toString() {
        return "SysLog(id=" + this.getId() + ", type=" + this.getType() + ", title=" + this.getTitle() + ", createBy=" + this.getCreateBy() + ", createTime=" + this.getCreateTime() + ", updateTime=" + this.getUpdateTime() + ", remoteAddr=" + this.getRemoteAddr() + ", userAgent=" + this.getUserAgent() + ", requestUri=" + this.getRequestUri() + ", method=" + this.getMethod() + ", params=" + this.getParams() + ", time=" + this.getTime() + ", exception=" + this.getException() + ", serviceId=" + this.getServiceId() + ", delFlag=" + this.getDelFlag() + ")";
    }
}
