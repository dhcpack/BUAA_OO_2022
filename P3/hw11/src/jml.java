  /*@ public nomal_behavior
    @ requires containsMessage(id) && (getMessage(id) instance of AdvertiseMessage) && getMessage(id).getType() == 0 &&
    @          getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
    @          getMessage(id).getPerson1() != getMessage(id).getPerson2();
    @ assignable messages, advertiseHeatList;
    @ assignable getMessage(id).getPerson2().messages;
    @ assignable getMessage(id).getPerson1().socialValue, getMessage(id).getPerson2().socialValue;
    @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
    @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
    @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
    @ ensures \old(getMessage(id)).getPerson1().getSocialValue() ==
    @         \old(getMessage(id).getPerson1().getSocialValue()) + \old(getMessage(id)).getSocialValue() &&
    @         \old(getMessage(id)).getPerson2().getSocialValue() ==
    @         \old(getMessage(id).getPerson2().getSocialValue()) + \old(getMessage(id)).getSocialValue();
    @ ensures (\exists int i; 0 <= i && i < advertiseIdList.length && advertiseIdList[i] == ((AdvertiseMessage)\old(getMessage(id))).getAdvertiseId();
    @         advertiseHeatList[i] == \old(advertiseHeatList[i]) + 1);
    @ ensures (\forall int i; 0 <= i && i < \old(getMessage(id).getPerson2().getMessages().size());
    @          \old(getMessage(id)).getPerson2().getMessages().get(i+1) == \old(getMessage(id).getPerson2().getMessages().get(i)));
    @ ensures \old(getMessage(id)).getPerson2().getMessages().get(0).equals(\old(getMessage(id)));
    @ ensures \old(getMessage(id)).getPerson2().getMessages().size() == \old(getMessage(id).getPerson2().getMessages().size()) + 1;
    @ also
    @ public normal_behavior
    @ requires containsMessage(id) && (getMessage(id) instance of AdvertiseMessage) && getMessage(id).getType() == 1 &&
    @           getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1());
    @ assignable people[*].socialValue, messages, advertiseHeatList;
    @ ensures !containsMessage(id) && messages.length == \old(messages.length) - 1 &&
    @         (\forall int i; 0 <= i && i < \old(messages.length) && \old(messages[i].getId()) != id;
    @         (\exists int j; 0 <= j && j < messages.length; messages[j].equals(\old(messages[i]))));
    @ ensures (\forall Person p; \old(getMessage(id)).getGroup().hasPerson(p); p.getSocialValue() ==
    @         \old(p.getSocialValue()) + \old(getMessage(id)).getSocialValue());
    @ ensures (\forall int i; 0 <= i && i < people.length && !\old(getMessage(id)).getGroup().hasPerson(people[i]);
    @          \old(people[i].getSocialValue()) == people[i].getSocialValue());
    @ ensures (\exists int i; 0 <= i && i < advertiseIdList.length && advertiseIdList[i] == ((AdvertiseMessage)\old(getMessage(id))).getAdvertiseId();
    @         advertiseHeatList[i] == \old(advertiseHeatList[i]) + 1);
    @ also
    @ public exceptional_behavior
    @ signals (MessageIdNotFoundException e) !containsMessage(id);
    @ signals (NotAdvertiseException e) !(getMessage(id) instance of AdvertiseMessage);
    @ signals (RelationNotFoundException e) containsMessage(id) && (getMessage(id) instance of AdvertiseMessage) &&
    @          getMessage(id).getType() == 0 && !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()));
    @ signals (PersonIdNotFoundException e) containsMessage(id) && (getMessage(id) instance of AdvertiseMessage) &&
    @          getMessage(id).getType() == 0 && !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()));
    @*/
    public void sendAdvertiseMessage(int id) throws MessageIdNotFoundException, NotAdvertiseException, RelationNotFoundException, PersonIdNotFoundException;


  /*@ public nomal_behavior
    @ requires (\exists int i; 0 <= i && i < advertiseIdList.length; advertiseIdList[i] == id);
    @ ensures (\exists int i; 0 <= i && i < advertiseIdList.length; advertiseIdList[i] == id &&
    @          \result == advertiseHeatList[i]);
    @ also
    @ public exceptional_behavior
    @ signals (GoodIdNotFoundException e) !AdvertiseIdList.contains(id);
    @*/
    public /*@ pure @*/ int setPrice(int id) throws GoodIdNotFoundException;



  /*@ public normal_behavior
    @ requires (\exists int i; 0 <= i && i < advertiseIdList.length; advertiseIdList[i] == id);
    @ assignable money;
    @ ensures (\exists int i; 0 <= i && i < advertiseIdList.length; advertiseIdList[i] == id &&
    @          money == \old(money) - advertiseHeatList[i]);
    @ also
    @ public exceptional_behavior
    @ signals (GoodIdNotFoundException e) !AdvertiseIdList.contains(id);
    @*/
    public void buyProduct(int id) throws GoodIdNotFoundException;
