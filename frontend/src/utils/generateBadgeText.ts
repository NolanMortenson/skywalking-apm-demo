/**
 *
 * @param overSmallSize - media query at which point the 'members' suffix will disappear
 * @param memberCount - the amount of members in the team
 * @returns {string} the text the badge will display
 */
export default function generateBadgeText(
  overSmallSize: boolean,
  memberCount?: number
) {
  const teamSize = memberCount || 0;
  let badgeSuffix = overSmallSize ? ' members' : '';
  teamSize === 1 && (badgeSuffix = badgeSuffix.slice(0, -1));
  return teamSize + badgeSuffix;
}
