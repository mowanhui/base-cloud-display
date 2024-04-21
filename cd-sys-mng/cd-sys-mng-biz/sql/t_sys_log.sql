CREATE TABLE sys_mng.t_sys_log(
                          id VARCHAR2(32) NOT NULL,
                          log_id VARCHAR2(32),
                          log_type VARCHAR2(100),
                          opt_action VARCHAR2(100),
                          log_tag VARCHAR2(255),
                          log_source VARCHAR2(100),
                          log_category VARCHAR2(100),
                          title VARCHAR2(900),
                          remote_addr VARCHAR2(255),
                          user_agent VARCHAR2(255),
                          request_uri VARCHAR2(255),
                          method VARCHAR2(90),
                          params VARCHAR2(900),
                          time INT,
                          exception CLOB,
                          response CLOB,
                          client_id VARCHAR2(32),
                          create_by VARCHAR2(32),
                          create_time DATE,
                          update_by VARCHAR2(32),
                          update_time DATE,
                          delete_by VARCHAR2(32),
                          delete_time DATE,
                          is_deleted VARCHAR2(1),
                          PRIMARY KEY (id)
);

COMMENT ON TABLE sys_mng.t_sys_log IS '系统日志';
COMMENT ON COLUMN sys_mng.t_sys_log.id IS '唯一ID';
COMMENT ON COLUMN sys_mng.t_sys_log.log_id IS '日志ID';
COMMENT ON COLUMN sys_mng.t_sys_log.log_type IS '日志类型，字典：log_type';
COMMENT ON COLUMN sys_mng.t_sys_log.opt_action IS '操作行为，字典：opt_action';
COMMENT ON COLUMN sys_mng.t_sys_log.log_tag IS '日志标签';
COMMENT ON COLUMN sys_mng.t_sys_log.log_source IS '日志来源：log_source';
COMMENT ON COLUMN sys_mng.t_sys_log.log_category IS '日志种类：log_category';
COMMENT ON COLUMN sys_mng.t_sys_log.title IS '标题';
COMMENT ON COLUMN sys_mng.t_sys_log.remote_addr IS '远程地址';
COMMENT ON COLUMN sys_mng.t_sys_log.user_agent IS '用户代理';
COMMENT ON COLUMN sys_mng.t_sys_log.request_uri IS '请求uri';
COMMENT ON COLUMN sys_mng.t_sys_log.method IS '操作方法';
COMMENT ON COLUMN sys_mng.t_sys_log.params IS '请求参数';
COMMENT ON COLUMN sys_mng.t_sys_log.time IS '请求时长';
COMMENT ON COLUMN sys_mng.t_sys_log.exception IS '异常信息';
COMMENT ON COLUMN sys_mng.t_sys_log.response IS '响应体';
COMMENT ON COLUMN sys_mng.t_sys_log.client_id IS '客户端标识';
COMMENT ON COLUMN sys_mng.t_sys_log.create_by IS '创建人';
COMMENT ON COLUMN sys_mng.t_sys_log.create_time IS '创建时间';
COMMENT ON COLUMN sys_mng.t_sys_log.update_by IS '更新人';
COMMENT ON COLUMN sys_mng.t_sys_log.update_time IS '更新时间';
COMMENT ON COLUMN sys_mng.t_sys_log.delete_by IS '删除人';
COMMENT ON COLUMN sys_mng.t_sys_log.delete_time IS '删除时间';
COMMENT ON COLUMN sys_mng.t_sys_log.is_deleted IS '逻辑删除';
