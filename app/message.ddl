create table public.message (
  send_count character varying(12) not null
  , chatroom_id character varying(12) not null
  , send_user_id character varying(50) not null
  , main_message character varying(500) not null
  , create_at timestamp(6) without time zone not null
  , primary key (send_count)
);
