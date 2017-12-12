/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.cards.k;

import java.util.UUID;
import mage.MageInt;
import mage.constants.SubType;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.Zone;
import mage.abilities.Ability;
import mage.abilities.condition.Condition;
import mage.abilities.costs.common.TapSourceCost;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.decorator.ConditionalActivatedAbility;
import mage.abilities.effects.common.GainLifeEffect;
import mage.filter.FilterPlayer;
import mage.filter.predicate.ObjectSourcePlayer;
import mage.filter.predicate.ObjectSourcePlayerPredicate;
import mage.game.Game;
import mage.players.Player;
import mage.target.TargetPlayer;
import mage.target.common.TargetOpponent;

/**
 *
 * @author stevemarkham81
 */
public class KeeperOfTheLight extends CardImpl {

    public KeeperOfTheLight(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.CREATURE}, "{W}{W}");
        
        this.subtype.add(SubType.HUMAN);
        this.subtype.add(SubType.WIZARD);
        this.power = new MageInt(1);
        this.toughness = new MageInt(2);

        // {W}, {tap}: Choose target opponent who had more life than you did as you activated this ability. You gain 3 life.
	Ability ability = new ConditionalActivatedAbility(
		Zone.BATTLEFIELD,
		new GainLifeEffect(3),
		new ManaCostsImpl("{W}"),
		new KeeperOfTheLightCondition(),
		"{W}, {tap}: Choose target opponent who had more life than you did as you activated this ability. You gain 3 life.");
	ability.addCost(new TapSourceCost());
        FilterPlayer filter = new FilterPlayer();
        filter.add(new KeeperOfTheLightPredicate(ability));
	ability.addTarget(new TargetPlayer(1, 1, false, filter));
	this.addAbility(ability);
    }

    public KeeperOfTheLight(final KeeperOfTheLight card) {
        super(card);
    }

    @Override
    public KeeperOfTheLight copy() {
        return new KeeperOfTheLight(this);
    }
}

class KeeperOfTheLightCondition implements Condition {

    @Override
    public boolean apply(Game game, Ability source) {
	Player player = game.getPlayer(source.getControllerId());
	int lifeTotal = player.getLife();
	boolean conditionApplies = false;
	for ( UUID opponentUUID : game.getOpponents(source.getControllerId()) ) {
            conditionApplies |= (game.getPlayer(opponentUUID).getLife() > lifeTotal &&
		game.getPlayer(opponentUUID).canBeTargetedBy(source.getSourceObject(game), source.getControllerId(), game));
        }
	return conditionApplies;
    }
}

class KeeperOfTheLightPredicate implements ObjectSourcePlayerPredicate<ObjectSourcePlayer<Player>> {

    private final Ability ability;
    
    public KeeperOfTheLightPredicate(Ability ability) {
        super();
        this.ability = ability;
    }
    
    @Override
    public boolean apply(ObjectSourcePlayer<Player> input, Game game) {
        Player potentialTarget = input.getObject();
        UUID playerId = input.getPlayerId();
        if (potentialTarget == null || playerId == null) {
            return false;
        }

        int opponentLife = potentialTarget.getLife();
        int controllerLife = game.getPlayer(playerId).getLife();
        boolean activated = this.ability.isActivated();
        boolean isSelf = potentialTarget.getId().equals(playerId);
        boolean isOpponent = game.getPlayer(playerId).hasOpponent(potentialTarget.getId(), game);
        boolean hasMoreLife = controllerLife<opponentLife;
        boolean rValue = (!isSelf && isOpponent && (activated || hasMoreLife));
        return rValue;
    }
    
        @Override
    public String toString() {
        return "opponent who had more life than you did as you activated this ability";
    }
}
