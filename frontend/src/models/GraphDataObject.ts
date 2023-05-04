export default interface GraphDataObject {
  numberOfSprints: number;
  count: number;
  partialSuccessRate: number;
  cumulativeSuccessRate: number;
  endDate?: string;
}
