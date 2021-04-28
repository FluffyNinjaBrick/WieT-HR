import { Route, Redirect } from "react-router-dom";
import { useAuth } from "../auth/useAuth";

export default function PrivateRoute({ children, role, ...rest }) {
  let auth = useAuth();
  return (
    <Route
      {...rest}
      render={({ location }) =>
        auth.user && (role === "any" || auth.user.userRole === role) ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: "/login",
              state: { from: location },
            }}
          />
        )
      }
    />
  );
}
