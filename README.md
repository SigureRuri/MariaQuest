# MariaQuest
_A SpigotMC library to create quest easily_

## 目的
MariaQuestは、一般のRPGに登場するようなクエストをプログラム内から簡潔に定義することを目的としています。
そのため、ゲーム内でのクエスト定義の方法は用意しておらず、また、ゲーム内からクエストを受注する方法もデフォルトではできません。
使用時に各々で実装していただくようお願いします。

## クエスト定義
クエストは`org.github.shur.mariaquest.quest.Quest`クラスを使用して定義します。  
IDのみコンストラクタで渡し、以降のパラーメタはすべてオーバーライドで行います。

コンストラクタの値
|          名前           |             型              |            説明            |
|          :-:           |             :-:             |            :-:            |
|           id           |           QuestId           | 一意なクエストのID           |


オーバーライドする値
|          名前           |    タイプ   |             型              |            説明            | オーバーライド必須 |
|          :-:           |     :-:    |             :-:             |            :-:            |       :-:       |
|          name          |    value   |            String           | 表示される名前               |       yes       |
|       description      |    value   |         List<String>        | 表示される説明               |        no       |
|        missions        |    value   |       List<Mission<*>>      | クリアに必要なミッション(後述) |        no       |
|    timeLimitSeconds    |    value   |             Long?           | クエストのタイムリミット      |        no       |
|    coolTimeSeconds     |    value   |             Long?           | クールタイム                |        no       |
|     orderableTimes     |    value   |              Int?           | 最大受注可能回数             |        no       |
|     requiredQuests     |    value   |          List<QuestId>      | 前提クエスト                 |        no       |
| requirementDescription |    value   |          List<String        | 表示される受注条件            |        no       |
|     canByOrderedBy     |  function  |     (Player) -> Boolean     | 受注条件                    |        no       |
|         onStart        |  function  |       (Player) -> Unit      | スタート時に行う動作          |        no       |
|         onClear        |  function  |       (Player) -> Unit      | クリア時に行う動作            |        no       |
|         onGiveUp       |  function  |       (Player) -> Unit      | ギブアップ時に行う動作         |        no       |

### ミッション定義
ミッションは、`org.github.shur.mariaquest.quest.mission.Mission`を拡張するクラスです。
通常、定義に必要な値はコンストラクタで渡します。  
[ミッション一覧](./src/main/java/com/github/shur/mariaquest/quest/mission)

以下は通常のコンストラクタで使用する値の一覧です。
`T`には、下記の`filter`で使用されるクラスが振られます。

|      名前     |                     型                     |                  説明                   |
|      :-:      |                    :-:                    |                   :-:                   |
|     goal      |                    Int                    | 達成までの回数                            |
|    onStart    |             (Player -> Unit)              | スタート時に行う動作                       |
| onChangeCount | (Player, before: Int, after: Int) -> Unit | カウント変更時に行う動作                    |
|    onClear    |             (Player) -> Unit              | ミッションクリア時に行う動作                |
|    onGiveUp   |             (Player) -> Unit              | ミッション断念時に行う動作                  |
|     filter    |          (Player, T) -> Boolean           | トリガーが引かれた際にカウントを増やすかどうか |

## クエストの登録
MariaQuestで扱うすべてのクエストは、`QuestManager`に登録する必要があります。

```kotlin
val questManager = MariaQuest.questManager
questManager.register(quest)
```
で登録してください。

## 自作ミッションの登録方法
TODO: 執筆中