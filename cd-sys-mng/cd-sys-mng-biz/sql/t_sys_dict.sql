CREATE TABLE sys_mng.t_sys_dict(
                           id VARCHAR2(100) NOT NULL,
                           dict_id VARCHAR2(100),
                           parent_id VARCHAR2(100),
                           dict_code VARCHAR2(255),
                           dict_name VARCHAR2(255),
                           dict_type_code VARCHAR2(32),
                           dict_type_name VARCHAR2(900),
                           remark VARCHAR2(255),
                           sort INT,
                           is_enable VARCHAR2(1),
                           create_by VARCHAR2(100),
                           create_time DATE,
                           update_by VARCHAR2(100),
                           update_time DATE,
                           delete_by VARCHAR2(100),
                           delete_time DATE,
                           is_deleted VARCHAR2(1),
                           value1 VARCHAR2(255),
                           value2 VARCHAR2(255),
                           value3 VARCHAR2(255),
                           PRIMARY KEY (id)
);

COMMENT ON TABLE sys_mng.t_sys_dict IS '字典表';
COMMENT ON COLUMN sys_mng.t_sys_dict.id IS '唯一ID';
COMMENT ON COLUMN sys_mng.t_sys_dict.dict_id IS '字典Id';
COMMENT ON COLUMN sys_mng.t_sys_dict.parent_id IS '父Id';
COMMENT ON COLUMN sys_mng.t_sys_dict.dict_code IS '字典代码';
COMMENT ON COLUMN sys_mng.t_sys_dict.dict_name IS '字典名称';
COMMENT ON COLUMN sys_mng.t_sys_dict.dict_type_code IS '字典类型代码：-00-00-';
COMMENT ON COLUMN sys_mng.t_sys_dict.dict_type_name IS '字典类型名称：-状态-';
COMMENT ON COLUMN sys_mng.t_sys_dict.remark IS '备注';
COMMENT ON COLUMN sys_mng.t_sys_dict.sort IS '排序';
COMMENT ON COLUMN sys_mng.t_sys_dict.is_enable IS '是否启用';
COMMENT ON COLUMN sys_mng.t_sys_dict.create_by IS '创建人';
COMMENT ON COLUMN sys_mng.t_sys_dict.create_time IS '创建时间';
COMMENT ON COLUMN sys_mng.t_sys_dict.update_by IS '更新人';
COMMENT ON COLUMN sys_mng.t_sys_dict.update_time IS '更新时间';
COMMENT ON COLUMN sys_mng.t_sys_dict.delete_by IS '删除人';
COMMENT ON COLUMN sys_mng.t_sys_dict.delete_time IS '删除时间';
COMMENT ON COLUMN sys_mng.t_sys_dict.is_deleted IS '逻辑删除';
