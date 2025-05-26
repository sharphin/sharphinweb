create table public.block_list (
  block_user_id character varying(50) not null,
  blocker_id character varying(50) not null,
  create_at timestamp(6) without time zone not null,
  primary key (block_user_id, blocker_id)
);
