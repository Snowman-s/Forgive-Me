# Forgive-Me
暇を持て余した神々的なJVM言語

## セットアップ
### 必要な物
- JDK11以上で動作すると思います。JDK11以上のPATHを通してください。
### ソースファイルについて
- 拡張子は、`*.forgive` としてください。
- 識別子は全て半角英数字で記述する必要があります。
### 手順
1. `create.bat` を同じ作業ディレクトリで実行。
2. `Forgive-Me/` を環境変数PATHに追加。
3. `forgive -h` を実行。
### testworld
- testworld 内はgitに無視されるので自由に使えます。

## 文法
1. あらゆる識別子において大文字小文字は区別されない。
2. 全て文の終了時にはピリオドまたは「!」を書く必要がある。この二つは等価である。
3. この言語において、「world」及び「世界」はいわゆるint型変数を指す。
4. この言語において、「ヒト」及び「人数」はいわゆる変数の中身を指す。
5. 最初の言葉が後に示す識別子ではない場合、その文は終了まで無視される。
6. 数値が要求されている箇所では代わりに「as [world name]」と書ける。このようにすると、[world name]の人数を数値として用いることができる。

### 世界宣言
- Declare [world name]. - [world name]の名前の世界を宣言する。任意のタイミングで使用可能。その世界の人数は0人で初期化される。既に存在していた同じ名前の世界は破壊される。
  - 何らかの要因でこの文よりも前の文に戻ったとしてもこの文で宣言した世界は破棄されない。但し実質的に宣言の前で世界を参照するのは不可能である。

### 外部との接触
- Migrate [world name]. - キーボード入力を受け付け入力された数値の人数を[world name]に住まわせる。元々住んでいたヒトは消去される。入力の数値変換に失敗したら例外が投げられる。

### 記録を残す
- Say [words...]. - Sayの後に続く、ピリオドまでの文を全て表示し、改行する。
- Forgive [world name]. - [world name]に住むヒトを全て赦し、赦した人数を次の形式で表示し改行する：「(人数) people were forgiven.」。なお、これによって世界の人数が変更されることは無い。

### 人数操作
- Live [world name] [number]. - [world name]の人数を[number(数値)]の人数にする。

- Add [world name] [number]. - [world name]の人数を[number(数値)]だけ増やす。
- Subtract [world name] [number]. - [world name]の人数を[number(数値)]だけ減らす。
- Multiply [world name] [number]. - [world name]の人数を[number(数値)]だけ乗ずる。
- Divide [world name] [number]. - [world name]の人数を[number(数値)]で除する。
- Mod [world name] [number]. - [world name]の人数を[number(数値)]で除した余りにする。

- Reverse [world name]. - [world name]の裏をとり、逆符号かつ同じ絶対値の人数にする。つまり-1を掛け合わせる。

### しおり
- Bookmark [bookmark name] - この文の位置に[bookmark name]の名前の栞をはさみ、後に特定の文で参照できる。

- Open [bookmark name] - [bookmark name]の栞が挟まれた位置に移動してまた読み始める。
- Open-Positive [bookmark name] [number] - [number]が1以上の場合、[bookmark name]の栞が挟まれた位置に移動してまた読み始める。

### コメント
- Reminder [comments...] - コメントを書くために特別に予約された識別子。この文は無視される。前述のとおり定義されない識別子は無視されるのでそれをコメントとして用いることもできる。