CREATE TABLE tasks
(
    id                 numeric(38) DEFAULT nextval('seq_tasks'::regclass) NOT NULL,
    task_type_id       varchar(38)                                        NULL,
    device_id          varchar(30)                                        NULL,
    created_at         timestamptz(6)                                     NULL,
    task_status_id     varchar(38)                                        NULL,
    CONSTRAINT tasks_pkey PRIMARY KEY (id)
);