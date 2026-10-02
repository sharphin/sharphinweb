# sharphin

## ローカルで動かす

必要なもの: JDK 17 以上、PostgreSQL（Docker があれば同梱の compose で起動可）

```sh
cd app/sharphin

# 1. DB を起動（Docker を使う場合）
docker compose up -d
#    Docker を使わない場合は PostgreSQL に以下を作成しておく
#    create user sharphin password 'fk5i9dtu';
#    create database sharphin owner sharphin;

# 2. アプリを起動（テーブルは schema.sql から自動作成される）
./mvnw spring-boot:run        # Windows は mvnw.cmd spring-boot:run
```

http://localhost:8080/sighup でユーザー登録 → http://localhost:8080/sighin でログイン。

### 設定（環境変数で上書き可）

| 変数 | 既定値 |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/sharphin` |
| `DB_USERNAME` | `sharphin` |
| `DB_PASSWORD` | `fk5i9dtu` |
| `ICON_DIR` | `~/sharphin/icon`（アイコン画像の保存先） |

## サーバーに置く場合

小さいインスタンス上でビルドすると Maven の依存ダウンロードでディスク/メモリが足りなくなりやすい。
手元でビルドして jar だけ転送するのが楽。

```sh
./mvnw -DskipTests package
scp target/sharphin-0.0.1-SNAPSHOT.jar <server>:
# サーバー側
DB_URL=... DB_PASSWORD=... java -jar sharphin-0.0.1-SNAPSHOT.jar
```
