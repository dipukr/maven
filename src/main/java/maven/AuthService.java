package maven;

public class AuthService {

	private static final AuthService INSTANCE = new AuthService();

	private UserDAO userDAO = new UserDAO();

	public boolean register(String username, String password) {
		if (userDAO.find(username) != null) {
			System.out.println("User already exists.");
			return false;
		}
		User user = new User(username, password);
		userDAO.save(user);
		return true;
	}

	public String login(String username, String password) {
		return userDAO.find(username)
				.filter(u -> u.password().equals(password))
				.map(u -> JwtUtils.generateToken(username))
				.orElse(null);
	}

	public boolean verify(String token) {
		return JwtUtils.verifyToken(token) != null;
	}

	public static AuthService getInstance() {
		return INSTANCE;
	}
}
