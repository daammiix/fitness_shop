export default {
  setUser(state, payload) {
    state.token = payload.token;
    state.user = payload.user;
  },
  setToken(state, payload){
    state.token = payload.token;
  },
  setUserData(state, payload){
    state.user = payload.user;
  },
	addCustomerVisitedFacilities(state, visitedFacility) {
		state.user.visitedFacilities ? state.user.visitedFacilities.push(visitedFacility) : state.user.visitedFacilities = [ visitedFacility ];
	},
	addtrainingToCustomerHistory(state, training) {
		state.user.trainingHistory ? state.user.trainingHistory.push(training) : state.user.trainingHistory = [ training ];
	}
}