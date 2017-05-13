# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ad (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  ad_image_url                  varchar(255),
  ad_click_url                  varchar(255),
  disabled                      boolean,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_ad primary key (id)
);

create table app_registry (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  app_name                      varchar(255),
  api_key                       varchar(255),
  major_version                 integer,
  minor_version                 integer,
  reset_password_url            varchar(255),
  reset_password_email_template varchar(255),
  confirm_email_url             varchar(255),
  confirm_email_email_template  varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_app_registry primary key (id)
);

create table appregistry_authenticateduser (
  app_registry_id               bigint not null,
  authenticated_user_id         bigint not null,
  constraint pk_appregistry_authenticateduser primary key (app_registry_id,authenticated_user_id)
);

create table audit_trail (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  description                   varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_audit_trail primary key (id)
);

create table authenticated_user (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  org_id                        bigint,
  password                      varchar(255),
  salt                          varbinary(255),
  fname                         varchar(255),
  lname                         varchar(255),
  email                         varchar(255),
  mobile_phone_number           varchar(255),
  pic_url                       varchar(255),
  slack_id                      varchar(255),
  last_ip                       varchar(255),
  last_browser                  varchar(255),
  mobile_verified               boolean,
  mobile_verification_code_last_sent datetime,
  mobile_verification_code_sent boolean,
  mobile_verification_code      varchar(255),
  mobile_verification_code_generated boolean,
  mobile_verification_code_date_generated datetime,
  link_uuid                     varchar(255),
  email_verified                boolean,
  email_verified_date           datetime,
  email_verification_last_sent  datetime,
  email_verification_sent       boolean,
  email_verification_ticket     varchar(255),
  email_verification_ticket_generated boolean,
  email_verification_ticket_date_generated datetime,
  disabled                      boolean,
  probation                     boolean,
  email_bounced                 boolean,
  proxy_email_id                varchar(255),
  session_uuid                  varchar(255),
  publishable_uuid              varchar(255),
  force_change_password         boolean,
  last_login                    datetime,
  password_reminder             varchar(255),
  password_reset_required       boolean,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_authenticated_user primary key (id)
);

create table authenticated_user_security_role (
  authenticated_user_id         bigint not null,
  security_role_id              bigint not null,
  constraint pk_authenticated_user_security_role primary key (authenticated_user_id,security_role_id)
);

create table authenticated_user_user_permission (
  authenticated_user_id         bigint not null,
  user_permission_id            bigint not null,
  constraint pk_authenticated_user_user_permission primary key (authenticated_user_id,user_permission_id)
);

create table contact_form (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  email                         varchar(255),
  message                       TEXT,
  ip                            varchar(255),
  referer                       varchar(255),
  user_agent                    varchar(255),
  app                           varchar(255),
  other                         varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_contact_form primary key (id)
);

create table image_meta (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  access_url                    varchar(255),
  thumbnail_url                 varchar(255),
  bucket_name                   varchar(255),
  sort_order                    integer,
  original_file_name            varchar(255),
  original_suffix               varchar(255),
  mime_type                     varchar(255),
  disabled                      boolean,
  description                   varchar(255),
  first                         boolean,
  file_name_uuid                varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_image_meta primary key (id)
);

create table inbox_message (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  sender_id                     bigint,
  receiver_id                   bigint,
  message                       TEXT,
  seen_by_sender                boolean,
  seen_by_receiver              boolean,
  deleted                       boolean,
  spam                          boolean,
  saved                         boolean,
  expired                       boolean,
  subject                       varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_inbox_message primary key (id)
);

create table job (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  description                   varchar(255),
  status                        varchar(255),
  completion_note               varchar(255),
  started                       DATETIME,
  completed                     DATETIME,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_job primary key (id)
);

create table login_audit (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  note                          varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  outcome                       tinyint,
  ipaddress                     varchar(255),
  referer                       varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_login_audit primary key (id)
);

create table motd (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  message                       varchar(255),
  disabled                      boolean,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_motd primary key (id)
);

create table note (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  note                          TEXT,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_note primary key (id)
);

create table organization (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_organization primary key (id)
);

create table password_reset (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  email                         varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_password_reset primary key (id)
);

create table question_bank (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  question                      varchar(255),
  choice1                       varchar(255),
  choice2                       varchar(255),
  choice3                       varchar(255),
  choice4                       varchar(255),
  choice5                       varchar(255),
  topic_id                      bigint,
  free                          boolean,
  registered                    boolean,
  disabled                      boolean,
  answer                        integer,
  difficulty                    integer,
  solution_description          varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_question_bank primary key (id)
);

create table question_bank_app_registry (
  question_bank_id              bigint not null,
  app_registry_id               bigint not null,
  constraint pk_question_bank_app_registry primary key (question_bank_id,app_registry_id)
);

create table registrant (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  email                         varchar(255),
  ip                            varchar(255),
  referer                       varchar(255),
  user_agent                    varchar(255),
  app                           varchar(255),
  other                         varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_registrant primary key (id)
);

create table sms (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  from_phone_number             varchar(255),
  to_phone_number               varchar(255),
  direction                     integer,
  message_sid                   varchar(255),
  smssid                        varchar(255),
  account_sid                   varchar(255),
  body                          varchar(255),
  num_media                     varchar(255),
  orphan                        boolean,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint ck_sms_direction check (direction in (0,1)),
  constraint pk_sms primary key (id)
);

create table security_role (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  public_facing                 boolean,
  public_description            varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_security_role primary key (id)
);

create table site_visitor (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  ip                            varchar(255),
  referer                       varchar(255),
  app                           varchar(255),
  note                          varchar(255),
  content_type                  varchar(255),
  host                          varchar(255),
  cookies                       varchar(255),
  user_agent                    varchar(255),
  dnt                           varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_site_visitor primary key (id)
);

create table team (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  org_id                        bigint,
  name                          varchar(255),
  slack_url                     varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint uq_team_org_id unique (org_id),
  constraint pk_team primary key (id)
);

create table test_result (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  u_id                          bigint,
  test_name                     varchar(255),
  answered_correct              integer,
  total_questions               integer,
  score                         double,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_test_result primary key (id)
);

create table topic (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  disabled                      boolean,
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_topic primary key (id)
);

create table user_permission (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  permission_value              varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_user_permission primary key (id)
);

create table word_list (
  id                            bigint auto_increment not null,
  uuid                          varchar(255),
  name                          varchar(255),
  version                       bigint not null,
  created_at                    DATETIME not null,
  updated_at                    DATETIME not null,
  constraint pk_word_list primary key (id)
);

alter table appregistry_authenticateduser add constraint fk_appregistry_authenticateduser_app_registry foreign key (app_registry_id) references app_registry (id) on delete restrict on update restrict;
create index ix_appregistry_authenticateduser_app_registry on appregistry_authenticateduser (app_registry_id);

alter table appregistry_authenticateduser add constraint fk_appregistry_authenticateduser_authenticated_user foreign key (authenticated_user_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_appregistry_authenticateduser_authenticated_user on appregistry_authenticateduser (authenticated_user_id);

alter table authenticated_user add constraint fk_authenticated_user_org_id foreign key (org_id) references organization (id) on delete restrict on update restrict;
create index ix_authenticated_user_org_id on authenticated_user (org_id);

alter table authenticated_user_security_role add constraint fk_authenticated_user_security_role_authenticated_user foreign key (authenticated_user_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_authenticated_user_security_role_authenticated_user on authenticated_user_security_role (authenticated_user_id);

alter table authenticated_user_security_role add constraint fk_authenticated_user_security_role_security_role foreign key (security_role_id) references security_role (id) on delete restrict on update restrict;
create index ix_authenticated_user_security_role_security_role on authenticated_user_security_role (security_role_id);

alter table authenticated_user_user_permission add constraint fk_authenticated_user_user_permission_authenticated_user foreign key (authenticated_user_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_authenticated_user_user_permission_authenticated_user on authenticated_user_user_permission (authenticated_user_id);

alter table authenticated_user_user_permission add constraint fk_authenticated_user_user_permission_user_permission foreign key (user_permission_id) references user_permission (id) on delete restrict on update restrict;
create index ix_authenticated_user_user_permission_user_permission on authenticated_user_user_permission (user_permission_id);

alter table inbox_message add constraint fk_inbox_message_sender_id foreign key (sender_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_inbox_message_sender_id on inbox_message (sender_id);

alter table inbox_message add constraint fk_inbox_message_receiver_id foreign key (receiver_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_inbox_message_receiver_id on inbox_message (receiver_id);

alter table question_bank add constraint fk_question_bank_topic_id foreign key (topic_id) references topic (id) on delete restrict on update restrict;
create index ix_question_bank_topic_id on question_bank (topic_id);

alter table question_bank_app_registry add constraint fk_question_bank_app_registry_question_bank foreign key (question_bank_id) references question_bank (id) on delete restrict on update restrict;
create index ix_question_bank_app_registry_question_bank on question_bank_app_registry (question_bank_id);

alter table question_bank_app_registry add constraint fk_question_bank_app_registry_app_registry foreign key (app_registry_id) references app_registry (id) on delete restrict on update restrict;
create index ix_question_bank_app_registry_app_registry on question_bank_app_registry (app_registry_id);

alter table team add constraint fk_team_org_id foreign key (org_id) references organization (id) on delete restrict on update restrict;

alter table test_result add constraint fk_test_result_u_id foreign key (u_id) references authenticated_user (id) on delete restrict on update restrict;
create index ix_test_result_u_id on test_result (u_id);


# --- !Downs

alter table appregistry_authenticateduser drop constraint if exists fk_appregistry_authenticateduser_app_registry;
drop index if exists ix_appregistry_authenticateduser_app_registry;

alter table appregistry_authenticateduser drop constraint if exists fk_appregistry_authenticateduser_authenticated_user;
drop index if exists ix_appregistry_authenticateduser_authenticated_user;

alter table authenticated_user drop constraint if exists fk_authenticated_user_org_id;
drop index if exists ix_authenticated_user_org_id;

alter table authenticated_user_security_role drop constraint if exists fk_authenticated_user_security_role_authenticated_user;
drop index if exists ix_authenticated_user_security_role_authenticated_user;

alter table authenticated_user_security_role drop constraint if exists fk_authenticated_user_security_role_security_role;
drop index if exists ix_authenticated_user_security_role_security_role;

alter table authenticated_user_user_permission drop constraint if exists fk_authenticated_user_user_permission_authenticated_user;
drop index if exists ix_authenticated_user_user_permission_authenticated_user;

alter table authenticated_user_user_permission drop constraint if exists fk_authenticated_user_user_permission_user_permission;
drop index if exists ix_authenticated_user_user_permission_user_permission;

alter table inbox_message drop constraint if exists fk_inbox_message_sender_id;
drop index if exists ix_inbox_message_sender_id;

alter table inbox_message drop constraint if exists fk_inbox_message_receiver_id;
drop index if exists ix_inbox_message_receiver_id;

alter table question_bank drop constraint if exists fk_question_bank_topic_id;
drop index if exists ix_question_bank_topic_id;

alter table question_bank_app_registry drop constraint if exists fk_question_bank_app_registry_question_bank;
drop index if exists ix_question_bank_app_registry_question_bank;

alter table question_bank_app_registry drop constraint if exists fk_question_bank_app_registry_app_registry;
drop index if exists ix_question_bank_app_registry_app_registry;

alter table team drop constraint if exists fk_team_org_id;

alter table test_result drop constraint if exists fk_test_result_u_id;
drop index if exists ix_test_result_u_id;

drop table if exists ad;

drop table if exists app_registry;

drop table if exists appregistry_authenticateduser;

drop table if exists audit_trail;

drop table if exists authenticated_user;

drop table if exists authenticated_user_security_role;

drop table if exists authenticated_user_user_permission;

drop table if exists contact_form;

drop table if exists image_meta;

drop table if exists inbox_message;

drop table if exists job;

drop table if exists login_audit;

drop table if exists motd;

drop table if exists note;

drop table if exists organization;

drop table if exists password_reset;

drop table if exists question_bank;

drop table if exists question_bank_app_registry;

drop table if exists registrant;

drop table if exists sms;

drop table if exists security_role;

drop table if exists site_visitor;

drop table if exists team;

drop table if exists test_result;

drop table if exists topic;

drop table if exists user_permission;

drop table if exists word_list;

