package model.constraint.relation;

import miner.log.Log;
import miner.rule.Rule;
import model.Constraint;
import model.constraint.RelationConstraint;
import model.constraint.TimedConstraint;
import model.data.Decision;
import model.expression.ExistenceExpression;

public class ChainPrecedence extends RelationConstraint implements TimedConstraint {

	private static final long serialVersionUID = 2550791783451482036L;

	private long timeA;
	private long timeB;

	public ChainPrecedence(Decision activationDec, Decision deactivationDec,
			ExistenceExpression consequence, ExistenceExpression condition,
			long timeA, long timeB, boolean isOptional) {
		super(activationDec, deactivationDec, condition, consequence, isOptional);
		this.timeA = timeA;
		this.timeB = timeB;
	}

	public long getTimeA() {
		return timeA;
	}

	public void setTimeA(long timeA) {
		this.timeA = timeA;
	}

	public long getTimeB() {
		return timeB;
	}

	public void setTimeB(long timeB) {
		this.timeB = timeB;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" + getConsequenceExpression()
		+ ", " + getConditionExpression()
		+ ", " + getTimeA()
		+ ", " + getTimeB() + ")" + super.toString();
	}

	@Override
	protected String getTextualConstraint() {
		String verb;
		if(getConditionExpression().getNrOfElements() == 1)
			verb = " has to";
		else
			verb = " have to";
		return getConditionExpression().toTextualString() + verb + " directly precede "
		+ getConsequenceExpression().toTextualString() + getTimedText(this);
	}

	@Override
	public boolean equals(Object obj) {
		return equals(obj, true);
	}

	public boolean equals(Object obj, boolean checkDecisions) {
		if (this == obj)
			return true;
		if (!super.equals(obj, checkDecisions))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public Constraint getDecisionlessCopy() {
		return new ChainPrecedence(null, null, getConsequenceExpression(), getConditionExpression(),
				getTimeA(), getTimeB(), isOptional());
	}

	@Override
	public Rule evaluate(Log log, Rule seed) {
		return mineChainPrecedence(this.getActivationDecision(), this.getConditionExpression(),
				this.getConsequenceExpression(), log);
	}

	public static Rule mineChainPrecedence(Decision activationDecision,
			ExistenceExpression condition, ExistenceExpression consequence,
			Log log) {
		throw new IllegalArgumentException("TODO: still used?");//TODO: still used?
		//		int countOtherViolations_Traces = 0;
		//		int countNrOfConfirmations = 0;
		//		int countNrOfViolations = 0;
		//		long timeMin = -1;
		//		long timeMax = -1;
		//		HashSet<Integer> tracesPositive = new HashSet<>();
		//		HashSet<Integer> tracesNegative = new HashSet<>();
		//		HashSet<DecisionRule> usedDecisions = new HashSet<>();
		//		for(int i = 0; i < log.size(); i++) {
		//			Trace t = log.get(i);
		//			long activationTime = 0;
		//			if(activationDecision != null) {
		//				DecisionActivation decisionActivation = t.getDecisionActivation(activationDecision);
		//				if(decisionActivation != null) {
		//					activationTime = decisionActivation.getTime();
		//					usedDecisions.add(decisionActivation.getDecisionRule());
		//				} else
		//					activationTime = -1;
		//			}
		//			if(activationTime != -1) {
		//				List<ActivityEvent> acts = t.getRemainingActivityList(activationTime);
		//				int indexTo = lastIndexOf(consequence, acts, -1, -1);
		//				if(indexTo > -1) {
		//					boolean restIsViolating = false;
		//					boolean hasConformed = false;
		//					boolean hasViolated = false;
		//					while(true) {
		//						int indexFrom = lastIndexOf(condition, acts
		//								.subList(0, indexTo), -1,
		//								acts.get(indexTo).getStart());
		//						if(restIsViolating
		//								|| indexFrom == -1) {
		//							restIsViolating = true;
		//							hasViolated = true;
		//							countNrOfViolations++;
		//						} else {
		//							long firstTimingBefore = -1;
		//							for(int j = 0; j < indexTo; j++) {
		//								if(acts.get(j).getEnd()
		//										<= acts.get(indexTo).getStart())
		//									firstTimingBefore = acts.get(j).getEnd();
		//							}
		//							if(firstTimingBefore == -1) {
		//								restIsViolating = true;
		//								hasViolated = true;
		//								countNrOfViolations++;
		//							} else {
		//								if(acts.get(indexFrom).getEnd() == firstTimingBefore) {
		//									long countDiff = acts.get(indexTo).getStart()
		//											- acts.get(indexFrom).getEnd();
		//									if(countDiff < 0) {
		//										throw new IllegalArgumentException("countDiff < 0???");
		//									} else {
		//										countNrOfConfirmations++;
		//										hasConformed = true;
		//										if(timeMin == -1 || timeMin > countDiff)
		//											timeMin = countDiff;
		//										if(timeMax == -1 || timeMax < countDiff)
		//											timeMax = countDiff;
		//									}
		//								} else {
		//									hasViolated = true;
		//									countNrOfViolations++;
		//								}
		//							}
		//						}
		//						indexTo = lastIndexOf(consequence, acts, indexTo, -1);
		//						if(indexTo == -1)
		//							break;
		//					}
		//					if(!hasViolated && hasConformed)
		//						tracesPositive.add(i);
		//					else if(hasViolated && !hasConformed)
		//						tracesNegative.add(i);
		//					else
		//						countOtherViolations_Traces++;
		//				}
		//			}
		//		}
		//		//remove redundant decision rules, as these are not used anyway...
		//		if(countNrOfConfirmations+countNrOfViolations > 0
		//				&& activationDecision != null
		//				&& usedDecisions.size() != activationDecision.getRules().size())
		//			activationDecision.setRules(usedDecisions);
		//		Rule pos = null;
		//		Rule neg = null;
		//		if(Config.MINE_CHAINPRECEDENCE)
		//			pos = new Rule(new ChainPrecedence(activationDecision, condition, consequence, timeMin, timeMax, true),
		//					countNrOfConfirmations, countNrOfViolations,
		//					tracesPositive, tracesNegative.size() + countOtherViolations_Traces,
		//					log.size());
		//		if(Config.MINE_NOTCHAINPRECEDENCE)
		//			neg = new Rule(new ChainPrecedence(activationDecision, condition, consequence, 0, -1, false),
		//					countNrOfViolations, countNrOfConfirmations,
		//					tracesNegative, tracesPositive.size() + countOtherViolations_Traces,
		//					log.size());
		//		if(pos != null && neg != null) {
		//			Rule[] result = {pos, neg};
		//			return result;
		//		} else if(pos != null && neg == null) {
		//			Rule[] result = {pos};
		//			return result;
		//		} else if(pos == null && neg != null) {
		//			Rule[] result = {neg};
		//			return result;
		//		} else
		//			return null;
	}

	@Override
	public RelationConstraint getShallowCopy(ExistenceExpression condition,
			ExistenceExpression consequence) {
		return new ChainPrecedence(getActivationDecision(), getDeactivationDecision(),
				consequence, condition, getTimeA(), getTimeB(), isOptional());
	}
}