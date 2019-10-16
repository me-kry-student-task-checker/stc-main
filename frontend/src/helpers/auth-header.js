export function authHeader() {
	// return authorization header with jwt token
	let accessToken = localStorage.getItem('access_token');
	if (accessToken) {
		accessToken = 'Bearer ' + accessToken;
		return accessToken;
	} else {
		return '';
	}
}
