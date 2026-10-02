-- 起動時に毎回実行される (spring.sql.init.mode=always)。既存データを壊さないよう IF NOT EXISTS で作成する。
create table if not exists public.users (
  user_id character varying(100) not null,
  user_name character varying(100) not null,
  email character varying(100) not null,
  password character varying(100) not null,
  icon_path character varying(200),
  authority character varying(10) not null,
  disable boolean not null,
  create_at timestamp(6) without time zone not null,
  update_at timestamp(6) without time zone not null,
  primary key (user_id)
);

create table if not exists public.follow_user_list (
  follow_user_id character varying(50) not null,
  followed_user_id character varying(50) not null,
  chatroom_id character varying(12),
  mutual boolean default false not null,
  create_at timestamp(6) without time zone
);

create table if not exists public.block_list (
  block_user_id character varying(50) not null,
  blocker_id character varying(50) not null,
  create_at timestamp(6) without time zone not null,
  primary key (block_user_id, blocker_id)
);

create table if not exists public.message (
  send_count bigserial not null,
  chatroom_id character varying(12) not null,
  recieve_user_id character varying(50) not null,
  main_message character varying(500) not null,
  create_at timestamp(6) without time zone not null,
  primary key (send_count)
);

create table if not exists public.posts (
  post_id character varying(50) not null,
  poster_id character varying(50) not null,
  create_at timestamp(6) without time zone,
  primary key (post_id)
);

create table if not exists public.rooms (
  room_id character varying(50) not null,
  room_owner character varying(50) not null,
  room_name character varying(100) not null,
  icon_path character varying(200),
  create_at timestamp(6) without time zone not null,
  primary key (room_id)
);

create table if not exists public.room_member (
  room_id character varying(50) not null,
  room_member_id character varying(50) not null,
  member_auth character varying(10) not null,
  create_at timestamp(6) without time zone not null,
  primary key (room_id, room_member_id)
);

create table if not exists public.room_message (
  room_id character varying(50) not null,
  send_user_id character varying(50) not null,
  send_user_icon_path character varying(200),
  main_message character varying(500),
  create_at timestamp(6) without time zone
);
