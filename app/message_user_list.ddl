create table public.message_user_list (
  user_id_1 character varying(50) not null,
  user_id_2 character varying(50) not null,
  create_at timestamp(6) without time zone not null,
  primary key (user_id_1, user_id_2)
);