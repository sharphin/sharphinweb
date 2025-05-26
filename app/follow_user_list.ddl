create table public.follow_user_list (
  follow_user_id character varying(50) not null,
  followed_user_id character varying(50) not null,
  chatroom_id character varying(12) not null,
  mutual boolean default false not null,
  create_at timestamp(6) without time zone
);
