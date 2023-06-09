package com.oocourse.uml3.interact;

import com.oocourse.uml3.interact.common.InstructionType;
import com.oocourse.uml3.interact.common.OutputInformation;
import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.InputArgumentException;
import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.UserProcessException;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.exceptions.UmlParseException;
import com.oocourse.uml3.utils.json.InputWithJson;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppRunner {
    private static final PrintStream DEFAULT_ERROR_STREAM = System.err;
    private static final InputStream DEFAULT_INPUT_STREAM = System.in;
    private static final PrintStream DEFAULT_OUTPUT_STREAM = System.out;
    private static final String END_OF_MODEL_REGEXP = "^\\s*END_OF_MODEL\\s*$";
    private static final String BLANK_LINE_REGEXP = "^\\s*$";
    private static final InputWithJson<UmlElement, UmlParseException> MODEL_LINE_PARSER = UmlElement::loadFromExportedJson;
    private static final Function<String, String> ENCRYPTION = OutputEncryption::getEncryptedMessage;
    private static final String WHITE_SPACE_SPLITTER = "\\s+";
    private static final int RUN_RE_EXIT_CODE = 192;
    private final Class<? extends UserApi> interactionClass;
    private final Constructor<? extends UserApi> constructor;
    private final InputStream inputStream;
    private final PrintStream outputStream;
    private final PrintStream errorStream;
    private final List<UmlElement> elementList;
    private UserApi interaction = null;
    private final Map<InstructionType, ArgumentsProcessor<Exception>> PROCESSORS
            = Collections.unmodifiableMap(new HashMap<InstructionType, ArgumentsProcessor<Exception>>() {{
        put(InstructionType.CLASS_COUNT,
                AppRunner.this::runAsClassCount);
        put(InstructionType.CLASS_SUBCLASS_COUNT,
                AppRunner.this::runAsClassSubClassCount);
        put(InstructionType.CLASS_OPERATION_COUNT,
                AppRunner.this::runAsClassOperationCount);
        put(InstructionType.CLASS_OPERATION_VISIBILITY,
                AppRunner.this::runAsClassOperationVisibility);
        put(InstructionType.CLASS_OPERATION_COUPLING_DEGREE,
                AppRunner.this::runAsClassOperationCouplingDegree);
        put(InstructionType.CLASS_ATTR_COUPLING_DEGREE,
                AppRunner.this::runAsClassAttributeCouplingDegree);
        put(InstructionType.CLASS_IMPLEMENT_INTERFACE_LIST,
                AppRunner.this::runAsClassImplementInterfaceList);
        put(InstructionType.CLASS_DEPTH_OF_INHERITANCE,
                AppRunner.this::runAsClassDepthOfInheritance);

        put(InstructionType.STATE_COUNT,
                AppRunner.this::runAsStateCount);
        put(InstructionType.STATE_IS_CRITICAL_POINT,
                AppRunner.this::runAsStateIsCriticalPoint);
        put(InstructionType.TRANSITION_TRIGGER,
                AppRunner.this::runAsTransitionTrigger);

        put(InstructionType.PTCP_OBJ_COUNT,
                AppRunner.this::runAsPtcpObjCount);
        put(InstructionType.PTCP_CREATOR,
                AppRunner.this::runAsPtcpCreator);
        put(InstructionType.PTCP_LOST_AND_FOUND,
                AppRunner.this::runAsLostAndFound);
      }}
    );

    private AppRunningStatus status;

    private AppRunner(Class<? extends UserApi> interactionClass,
                      InputStream inputStream, PrintStream outputStream, PrintStream errorStream)
            throws NoSuchMethodException {
        this.interactionClass = interactionClass;
        this.constructor = this.interactionClass.getConstructor(UmlElement[].class);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.errorStream = errorStream;
        this.elementList = new ArrayList<>();
        this.status = AppRunningStatus.NOT_STARTED;
    }

    private AppRunner(Class<? extends UserApi> interactionClass) throws NoSuchMethodException {
        this(interactionClass, DEFAULT_INPUT_STREAM, DEFAULT_OUTPUT_STREAM, DEFAULT_ERROR_STREAM);
    }

    public static AppRunner newInstance(Class<? extends UserApi> interactionClass) throws NoSuchMethodException {
        return new AppRunner(interactionClass);
    }

    private static void printlnToStream(String message, PrintStream printStream) {
        printStream.println(ENCRYPTION.apply(message));
    }

    public Class<? extends UserApi> getInteractionClass() {
        return interactionClass;
    }

    public void run(String[] args) {
        try {
            beforeStartEvent();
            Scanner scanner = new Scanner(inputStream);
            while (this.status.isRunning() && scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineProcessEvent(line);
            }
            scanner.close();
            afterCompleteEvent();
        } catch (Exception e) {
            exceptionProcessEvent(e);
        }
    }

    private void exceptionProcessEvent(Exception e) {
        printlnAsError("Something wrong with your process.");
        e.printStackTrace();
        System.exit(RUN_RE_EXIT_CODE);
    }

    private void beforeStartEvent() {
        this.status = AppRunningStatus.PROCESSING_MODEL;
    }

    private void afterCompleteEvent() {
        this.status = AppRunningStatus.COMPLETED;
    }

    private void lineProcessEvent(String line)
            throws Exception {
        if (!isBlankLine(line)) {
            if (this.status == AppRunningStatus.PROCESSING_MODEL) {
                if (isEndOfModel(line)) {
                    endOfModelProcessEvent();
                } else {
                    modelProcessEvent(line);
                }
            } else if (this.status == AppRunningStatus.PROCESSING_INSTRUCTION) {
                instructionProcessEvent(line);
            }
        }
    }

    private void modelProcessEvent(String line) throws UmlParseException, ParseException {
        UmlElement umlElement = MODEL_LINE_PARSER.loadFromString(line);
        this.elementList.add(umlElement);
    }

    private void endOfModelProcessEvent()
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        UmlElement[] elements = new UmlElement[this.elementList.size()];
        for (int i = 0; i < this.elementList.size(); i++) {
            elements[i] = this.elementList.get(i);
        }

        interaction = newInteractionInstance(elements);
        try {
            interaction.checkForAllRules();
            this.status = AppRunningStatus.PROCESSING_INSTRUCTION;
        } catch (PreCheckRuleException e) {
            printlnAsOutput(e.getMessage());
            this.status = AppRunningStatus.EXIT;
        }
    }

    private void instructionProcessEvent(String line) throws Exception {
        List<Object> arguments;
        try {
            arguments = InstructionType.COMMON_PARSER.parse(line.trim(), WHITE_SPACE_SPLITTER);
        } catch (InputArgumentException e) {
            printlnAsError("Error, invalid instruction type.");
            return;
        }

        InstructionType type = (InstructionType) arguments.get(0);
        runAsArguments(type, line);
    }

    private void runAsArguments(InstructionType type, String line) throws Exception {
        List<Object> arguments;
        try {
            arguments = InstructionType.INSTRUCTION_PARSERS.get(type).parse(line.trim(), WHITE_SPACE_SPLITTER);
        } catch (InputArgumentException e) {
            printlnAsError("Error, invalid instruction format.");
            return;
        }

        if (!PROCESSORS.containsKey(type)) {
            printlnAsError("Sorry, processors not supported by app engine yet.");
            return;
        }

        OutputInformation information;
        try {
            information = PROCESSORS.get(type).process(type, arguments);
        } catch (UserProcessException exception) {
            printlnAsOutput(exception.getMessage());
            return;
        }

        printlnAsOutput(information.getMessage());
    }

    private UserApi newInteractionInstance(UmlElement[] elements)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return this.constructor.newInstance(new Object[]{elements});
    }

    private boolean isEndOfModel(String line) {
        return (line != null) && (line.matches(END_OF_MODEL_REGEXP));
    }

    private boolean isBlankLine(String line) {
        return (line != null) && (line.matches(BLANK_LINE_REGEXP));
    }

    private void printlnAsOutput(String message) {
        printlnToStream(message, this.outputStream);
    }

    private void printlnAsError(String message) {
        printlnToStream(message, this.errorStream);
    }

    private OutputInformation runAsClassCount(
            InstructionType type, List<Object> arguments) throws Exception {
        int count = interaction.getClassCount();
        return new OutputInformation(
                String.format("Total class count is %d.", count)
        );
    }

    private OutputInformation runAsClassSubClassCount(
            InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int count = interaction.getClassSubClassCount(className);
        return new OutputInformation(
                String.format("Ok, subclass count of class \"%s\" is %d.", className, count)
        );
    }

    private OutputInformation runAsClassOperationCount(
            InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int count = interaction.getClassOperationCount(className);
        return new OutputInformation(
                String.format("Ok, operation count of class \"%s\" is %d.", className, count)
        );
    }

    private OutputInformation runAsClassOperationVisibility(
            InstructionType type, List<Object> arguments)
            throws Exception {
        String className = (String) arguments.get(1);
        String methodName = (String) arguments.get(2);
        Map<Visibility, Integer> analysis = interaction.getClassOperationVisibility(className, methodName);
        return new OutputInformation(
                String.format(
                        "Ok, operation visibility of method \"%s\" in class \"%s\" is %s.",
                        methodName, className,
                        Arrays.stream(Visibility.values())
                                .map(visibility -> String.format("%s: %s",
                                        visibility.getCommonString(),
                                        analysis.getOrDefault(visibility, 0)))
                                .collect(Collectors.joining(", "))
                )
        );
    }

    private OutputInformation runAsClassOperationCouplingDegree(
            InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        String methodName = (String) arguments.get(2);
        List<Integer> couplings =
                new ArrayList<>(interaction.getClassOperationCouplingDegree(className, methodName));
        Collections.sort(couplings);
        return new OutputInformation(
                String.format("Ok, method \"%s\" in class \"%s\" has coupling degree: %s.",
                        methodName, className,
                        couplings.stream()
                                .map(String::valueOf)
                                .collect(Collectors.joining(", "))
                ));

    }

    private OutputInformation runAsClassAttributeCouplingDegree(
            InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int coupling = interaction.getClassAttributeCouplingDegree(className);
        return new OutputInformation(
                String.format(
                        "Ok, attributes in class \"%s\" has coupling degree %d.",
                        className, coupling
                )
        );
    }

    private OutputInformation runAsClassImplementInterfaceList(InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        List<String> interfaces =
                new ArrayList<>(interaction.getClassImplementInterfaceList(className));
        Collections.sort(interfaces);
        return new OutputInformation(
                String.format(
                        "Ok, implement interfaces of class \"%s\" are (%s).",
                        className,
                        String.join(", ", interfaces)
                )
        );
    }

    private OutputInformation runAsClassDepthOfInheritance(
            InstructionType type, List<Object> arguments) throws Exception {
        String className = (String) arguments.get(1);
        int depth = interaction.getClassDepthOfInheritance(className);
        return new OutputInformation(
                String.format("Ok, depth of inheritance of class \"%s\" is %d.", className, depth)
        );
    }

    private OutputInformation runAsStateCount(InstructionType type, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        int count = interaction.getStateCount(stateMachineName);
        return new OutputInformation(
                String.format("Ok，state count of statemachine \"%s\" is %d.", stateMachineName, count)
        );
    }

    private OutputInformation runAsStateIsCriticalPoint(InstructionType type, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        String stateName = (String) arguments.get(2);
        boolean isCriticalPoint = interaction.getStateIsCriticalPoint(stateMachineName, stateName);
        return new OutputInformation(
                String.format("Ok, state \"%s\" in statemachine \"%s\" is %sa critical point.",
                        stateName, stateMachineName, isCriticalPoint ? "" : "not ")
        );
    }

    private OutputInformation runAsTransitionTrigger(InstructionType instructionType, List<Object> arguments) throws Exception {
        String stateMachineName = (String) arguments.get(1);
        String sourceStateName = (String) arguments.get(2);
        String targetStateName = (String) arguments.get(3);
        List<String> triggers = interaction.getTransitionTrigger(stateMachineName, sourceStateName, targetStateName);
        Collections.sort(triggers);
        return new OutputInformation(
                String.format("Ok, triggers of transition from state \"%s\" to state \"%s\" in statemachine \"%s\" are (%s).",
                        sourceStateName, targetStateName, stateMachineName, String.join(", ", triggers)
                )
        );
    }

    private OutputInformation runAsPtcpObjCount(InstructionType type, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        int count = interaction.getParticipantCount(interactionName);
        return new OutputInformation(
                String.format("Ok, participant count of umlinteraction \"%s\" is %d.",
                        interactionName, count)
        );
    }

    private OutputInformation runAsPtcpCreator(InstructionType type, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        String lifelineName = (String) arguments.get(2);
        UmlLifeline creator = interaction.getParticipantCreator(interactionName, lifelineName);
        return new OutputInformation(
                String.format("Ok, lifeline \"%s\" in umlinteraction \"%s\" can be created by \"%s\".",
                        lifelineName, interactionName, creator.getName())
        );
    }

    private OutputInformation runAsLostAndFound(InstructionType instructionType, List<Object> arguments) throws Exception {
        String interactionName = (String) arguments.get(1);
        String lifelineName = (String) arguments.get(2);
        Pair<Integer, Integer> count = interaction.getParticipantLostAndFound(interactionName, lifelineName);
        return new OutputInformation(
                String.format("Ok, incoming found message and outgoing lost message count of lifeline \"%s\" of umlinteraction \"%s\" is %d and %d.",
                        lifelineName, interactionName, count.getFirst(), count.getSecond())
        );
    }

    private enum AppRunningStatus {
        NOT_STARTED,
        PROCESSING_MODEL,
        PROCESSING_INSTRUCTION,
        COMPLETED,
        EXIT;

        boolean isStarted() {
            return (this != NOT_STARTED);
        }

        boolean isRunning() {
            return isStarted() && !isFinished();
        }

        boolean isFinished() {
            return (this == COMPLETED) || (this == EXIT);
        }
    }

    private interface ArgumentsProcessor<E extends Exception> {
        OutputInformation process(InstructionType type, List<Object> arguments) throws E;
    }
}
