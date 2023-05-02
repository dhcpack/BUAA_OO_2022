import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.RedEnvelopeMessage;

// assume all types are our own types

public class MyGlb {
    private static final HashMap<Integer, MyPpl> PEOPLES = new HashMap<>();
    private static final HashMap<Integer, Integer> FAMILY = new HashMap<>();
    private static final HashMap<MyConn, Integer> REL_VAL = new HashMap<>();
    private static final HashMap<Integer, MyGrp> GROUPS = new HashMap<>();
    private static final HashMap<Integer, MyMsg> MSGS = new HashMap<>();
    private static final HashMap<Integer, Integer> EMOJIS = new HashMap<>();

    public static HashMap<MyConn, Integer> myGetRels() {
        return REL_VAL;
    }

    public static boolean contains(int id) {
        return PEOPLES.containsKey(id);
    }

    public static MyPpl getPerson(int id) {
        return PEOPLES.getOrDefault(id, null);
    }

    public static void addPerson(MyPpl p) throws MyEqPpId {
        int id = p.getId();
        if (contains(id)) {
            throw new MyEqPpId(id);
        }
        PEOPLES.put(id, p);

        // a new family
        FAMILY.put(id, id);
    }

    public static void addRelation(int id1, int id2, int value) throws MyPpIdNotFn, MyEqRel {
        if (!contains(id1)) {
            throw new MyPpIdNotFn(id1);
        }
        if (!contains(id2)) {
            throw new MyPpIdNotFn(id2);
        }
        MyPpl p1 = getPerson(id1);
        MyPpl p2 = getPerson(id2);
        if (p1.isLinked(p2)) {
            throw new MyEqRel(id1, id2);
        }
        p1.myGetLinks().put(id2, value);
        p2.myGetLinks().put(id1, value);
        REL_VAL.put(new MyConn(id1, id2), value);

        // replace f1 to f2
        int f1 = FAMILY.get(id1);
        int f2 = FAMILY.get(id2);
        if (f1 != f2) {
            FAMILY.replaceAll((pId, family) -> family.equals(f1) ? f2 : family);
        }
    }

    public static int queryValue(int id1, int id2) throws MyPpIdNotFn, MyRelNotFn {
        if (!contains(id1)) {
            throw new MyPpIdNotFn(id1);
        }
        if (!contains(id2)) {
            throw new MyPpIdNotFn(id2);
        }
        MyPpl p1 = getPerson(id1);
        MyPpl p2 = getPerson(id2);
        if (!p1.isLinked(p2)) {
            throw new MyRelNotFn(id1, id2);
        }
        return p1.queryValue(p2);
    }

    public static int queryPeopleSum() {
        return PEOPLES.size();
    }

    public static boolean isCircle(int id1, int id2) throws MyPpIdNotFn {
        if (!contains(id1)) {
            throw new MyPpIdNotFn(id1);
        }
        if (!contains(id2)) {
            throw new MyPpIdNotFn(id2);
        }
        return FAMILY.get(id1).equals(FAMILY.get(id2));
    }

    public static int queryBlockSum() {
        HashSet<Integer> unique = new HashSet<>(FAMILY.values());
        return unique.size();
    }

    public static int queryLeastConnection(int beginId) throws MyPpIdNotFn {
        if (!contains(beginId)) {
            throw new MyPpIdNotFn(beginId);
        }
        int qlcResult = 0;
        HashSet<Integer> visited = new HashSet<>();
        PriorityQueue<MyPriorityItem<Integer>> queue = new PriorityQueue<>();
        queue.add(new MyPriorityItem<Integer>(0, beginId));
        while (!queue.isEmpty()) {
            MyPriorityItem<Integer> nextNode = queue.remove();
            if (visited.add(nextNode.getItem()) == false) {
                continue;
            }
            qlcResult += nextNode.getPriority();
            getPerson(nextNode.getItem()).myGetLinks().forEach((linkId, value) -> {
                if (!visited.contains(linkId)) {
                    queue.add(new MyPriorityItem<Integer>(value, linkId));
                }
            });
        }
        return qlcResult;
    }

    public static void addGroup(MyGrp g) throws MyEqGrpId {
        int id = g.getId();
        if (GROUPS.putIfAbsent(id, g) != null) {
            throw new MyEqGrpId(id);
        }
    }

    public static MyGrp getGroup(int id) {
        return GROUPS.getOrDefault(id, null);
    }

    public static void addToGroup(int ppId, int grpId) throws MyGrpIdNotFn, MyPpIdNotFn, MyEqPpId {
        MyGrp g = getGroup(grpId);
        if (g == null) {
            throw new MyGrpIdNotFn(grpId);
        }
        MyPpl p = getPerson(ppId);
        if (p == null) {
            throw new MyPpIdNotFn(ppId);
        }
        if (g.hasPerson(p)) {
            throw new MyEqPpId(ppId);
        }
        g.addPerson(p);
    }

    public static int queryGroupPeopleSum(int id) throws MyGrpIdNotFn {
        MyGrp g = getGroup(id);
        if (g == null) {
            throw new MyGrpIdNotFn(id);
        }
        return g.getSize();
    }

    public static int queryGroupValueSum(int id) throws MyGrpIdNotFn {
        MyGrp g = getGroup(id);
        if (g == null) {
            throw new MyGrpIdNotFn(id);
        }
        return g.getValueSum();
    }

    public static int queryGroupAgeVar(int id) throws MyGrpIdNotFn {
        MyGrp g = getGroup(id);
        if (g == null) {
            throw new MyGrpIdNotFn(id);
        }
        return g.getAgeVar();
    }

    public static void delFromGroup(int ppId, int grpId)
            throws MyGrpIdNotFn, MyPpIdNotFn, MyEqPpId {
        MyGrp g = getGroup(grpId);
        if (g == null) {
            throw new MyGrpIdNotFn(grpId);
        }
        MyPpl p = getPerson(ppId);
        if (p == null) {
            throw new MyPpIdNotFn(ppId);
        }
        if (!g.hasPerson(p)) {
            throw new MyEqPpId(ppId);
        }
        g.delPerson(p);
    }

    public static boolean containsMessage(int id) {
        return MSGS.containsKey(id);
    }

    public static void addMessage(MyMsg msg) throws MyEqMsgId, MyEmoIdNotFn, MyEqPpId {
        if (containsMessage(msg.getId())) {
            throw new MyEqMsgId(msg.getId());
        }
        if (msg instanceof EmojiMessage) {
            EmojiMessage emsg = (EmojiMessage) msg;
            if (!containsEmojiId(emsg.getEmojiId())) {
                throw new MyEmoIdNotFn(emsg.getEmojiId());
            }
        }
        if (msg.getType() == 0 && msg.getPerson1().equals(msg.getPerson2())) {
            throw new MyEqPpId(msg.getPerson1().getId());
        }
        MSGS.put(msg.getId(), msg);
    }

    public static MyMsg getMessage(int id) {
        return MSGS.getOrDefault(id, null);
    }

    public static void sendMessage(int id) throws MyRelNotFn, MyMsgIdNotFn, MyPpIdNotFn {
        MyMsg msg = getMessage(id);
        if (msg == null) {
            throw new MyMsgIdNotFn(id);
        }
        MyPpl ppSnd = (MyPpl) msg.getPerson1();
        if (msg.getType() == 0) {
            MyPpl ppTgt = (MyPpl) msg.getPerson2();
            if (!ppSnd.isLinked(ppTgt)) {
                throw new MyRelNotFn(ppSnd.getId(), ppTgt.getId());
            }
            // increase social value
            ppSnd.addSocialValue(msg.getSocialValue());
            ppTgt.addSocialValue(msg.getSocialValue());
            if (msg instanceof RedEnvelopeMessage) {
                // red - transfer money
                MyMsgRed redMsg = (MyMsgRed) msg;
                ppSnd.addMoney(-redMsg.getMoney());
                ppTgt.addMoney(redMsg.getMoney());
            }
            if (msg instanceof EmojiMessage) {
                // emo - increase popularity
                MyMsgEmoji emoMsg = (MyMsgEmoji) msg;
                int emoId = emoMsg.getEmojiId();
                EMOJIS.put(emoId, EMOJIS.get(emoId) + 1);
            }
            // add to messages
            ppTgt.myGetMsgs().add(0, msg);
        }
        if (msg.getType() == 1) {
            MyGrp g = (MyGrp) msg.getGroup();
            if (!g.hasPerson(ppSnd)) {
                throw new MyPpIdNotFn(ppSnd.getId());
            }
            // increase social value
            for (MyPpl ppTgt : g.myGetPeople().values()) {
                ppTgt.addSocialValue(msg.getSocialValue());
            }
            if (msg instanceof RedEnvelopeMessage) {
                // red - transfer money
                MyMsgRed redMsg = (MyMsgRed) msg;
                int singleMoney = redMsg.getMoney() / g.getSize();
                // decrease sender
                ppSnd.addMoney(-singleMoney * g.getSize());
                // increase target
                for (MyPpl ppTgt : g.myGetPeople().values()) {
                    ppTgt.addMoney(singleMoney);
                }
            }
            if (msg instanceof EmojiMessage) {
                // emo - increase popularity
                MyMsgEmoji emoMsg = (MyMsgEmoji) msg;
                int emoId = emoMsg.getEmojiId();
                EMOJIS.put(emoId, EMOJIS.get(emoId) + 1);
            }
        }
        // remove from global
        MSGS.remove(id);
    }

    public static int querySocialValue(int id) throws MyPpIdNotFn {
        MyPpl ppl = getPerson(id);
        if (ppl == null) {
            throw new MyPpIdNotFn(id);
        }
        return ppl.getSocialValue();
    }

    public static List<Message> queryReceivedMessages(int id) throws MyPpIdNotFn {
        MyPpl ppl = getPerson(id);
        if (ppl == null) {
            throw new MyPpIdNotFn(id);
        }
        return ppl.getReceivedMessages();
    }

    public static boolean containsEmojiId(int id) {
        return EMOJIS.containsKey(id);
    }

    public static void storeEmojiId(int id) throws MyEqEmoId {
        if (EMOJIS.putIfAbsent(id, 0) != null) {
            throw new MyEqEmoId(id);
        }
    }

    public static int queryMoney(int id) throws MyPpIdNotFn {
        MyPpl ppl = getPerson(id);
        if (ppl == null) {
            throw new MyPpIdNotFn(id);
        }
        return ppl.getMoney();
    }

    public static int queryPopularity(int id) throws MyEmoIdNotFn {
        if (!containsEmojiId(id)) {
            throw new MyEmoIdNotFn(id);
        }
        return EMOJIS.get(id);
    }

    public static int deleteColdEmoji(int minHeat) {
        HashSet<Integer> emRemove = new HashSet<>();
        EMOJIS.forEach((k, v) -> {
            if (v < minHeat) {
                emRemove.add(k);
            }
        });
        for (int removeId : emRemove) {
            EMOJIS.remove(removeId);
        }
        HashSet<Integer> msgRemove = new HashSet<>();
        MSGS.forEach((k, v) -> {
            if (v instanceof EmojiMessage && emRemove.contains(((EmojiMessage) v).getEmojiId())) {
                msgRemove.add(k);
            }
        });
        for (int removeId : msgRemove) {
            MSGS.remove(removeId);
        }
        return EMOJIS.size();
    }

    public static void clearNotices(int id) throws MyPpIdNotFn {
        MyPpl ppl = getPerson(id);
        if (ppl == null) {
            throw new MyPpIdNotFn(id);
        }
        ppl.myGetMsgs().removeIf(msg -> msg instanceof NoticeMessage);
    }

    public static int sendIndirectMessage(int id) throws MyMsgIdNotFn {
        MyMsg msg = getMessage(id);
        if (msg == null || msg.getType() == 1) {
            throw new MyMsgIdNotFn(id);
        }
        MyPpl ppSnd = (MyPpl) msg.getPerson1();
        MyPpl ppTgt = (MyPpl) msg.getPerson2();
        try {
            if (!isCircle(ppSnd.getId(), ppTgt.getId())) {
                return -1;
            }
        } catch (MyPpIdNotFn e) {
            e.printStackTrace(); // impossible
        }
        // increase social value
        ppSnd.addSocialValue(msg.getSocialValue());
        ppTgt.addSocialValue(msg.getSocialValue());
        if (msg instanceof RedEnvelopeMessage) {
            // red - transfer money
            MyMsgRed redMsg = (MyMsgRed) msg;
            ppSnd.addMoney(-redMsg.getMoney());
            ppTgt.addMoney(redMsg.getMoney());
        }
        if (msg instanceof EmojiMessage) {
            // emo - increase popularity
            MyMsgEmoji emoMsg = (MyMsgEmoji) msg;
            int emoId = emoMsg.getEmojiId();
            EMOJIS.put(emoId, EMOJIS.get(emoId) + 1);
        }
        // add to messages
        ppTgt.myGetMsgs().add(0, msg);
        // remove from global
        MSGS.remove(id);
        // find the shortest path from ppSnd to ppTgt, return its length
        return dijkLength(ppSnd, ppTgt);
    }

    private static int dijkLength(MyPpl ppFr, MyPpl ppTo) {
        HashSet<Integer> visited = new HashSet<>();
        HashSet<Integer> unvisited = new HashSet<>();
        HashMap<Integer, Integer> dist = new HashMap<>();
        unvisited.add(ppFr.getId());
        dist.put(ppFr.getId(), 0);
        while (!unvisited.isEmpty()) {
            int current = -1;
            for (int id : unvisited) {
                if (current == -1 || dist.getOrDefault(id, Integer.MAX_VALUE) < dist
                        .getOrDefault(current, Integer.MAX_VALUE)) {
                    current = id;
                }
            }
            final int curFin = current;
            unvisited.remove(curFin);
            visited.add(curFin);
            // found the unvisited with minimum distance, and moved it to visited
            MyPpl ppCur = getPerson(curFin);
            // consider its neighbors
            ppCur.myGetLinks().forEach((nei, value) -> {
                // not visited this neighbor
                if (!visited.contains(nei)) {
                    unvisited.add(nei);
                    // compute new distance
                    int distNew = dist.get(curFin) + value;
                    int distOld = dist.getOrDefault(nei, Integer.MAX_VALUE);
                    if (distNew < distOld) {
                        dist.put(nei, distNew);
                    }
                }
            });
        }
        return dist.getOrDefault(ppTo.getId(), -1);
    }
}
