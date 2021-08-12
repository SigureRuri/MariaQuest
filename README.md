# MariaQuest
_A SpigotMC library to create quest easily_

## 目的
MariaQuestは、一般のRPGに登場するようなクエストをプログラム内から簡潔に定義することを目的としています。
そのため、ゲーム内でのクエスト定義の方法は用意しておらず、また、ゲーム内からクエストを受注する方法もデフォルトではできません。
使用時に各々で実装していただくようお願いします。

## クエスト定義
クエストは`org.github.shur.mariaquest.quest.Quest`クラスを使用して定義します。
定義に必要な値はすべてコンストラクタで渡します。

以下はコンストラクタで使用する値の一覧です。  
上からコンストラクタに渡す順に記載しています。

|          名前           |             型              |            説明            |
|          :-:           |             :-:             |            :-:            |
|           id           |           QuestId           | 一意なクエストのID           |
|          name          |            String           | 表示される名前               |
|       description      |         List<String>        | 表示される説明               |
|        missions        |       List<Mission<*>>      | クリアに必要なミッション(後述) |
|    timeLimitSeconds    |             Long?           | クエストのタイムリミット      |
|    coolTimeSeconds     |             Long?           | クールタイム                |
|     orderableTimes     |              Int?           | 最大受注可能回数             |
|     requiredQuests     |          List<QuestId>      | 前提クエスト                 |
|      requirement       |     (Player) -> Boolean     | 受注条件                    |
| requirementDescription |          List<String        | 表示される受注条件            |
|         onStart        |       (Player) -> Unit      | スタート時に行う動作          |
|         onClear        |       (Player) -> Unit      | クリア時に行う動作            |
|         onGiveUp       |       (Player) -> Unit      | ギブアップ時に行う動作         |

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