import "./App.css";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
  Link,
} from "react-router-dom";
import Navigation from "./components/navigation/Navigation";
import LeavesView from "./components/leave/LeavesView";
import DelegationsView from "./components/delegation/DelegationsView";
import Profile from "./components/profile/Profile";
import ContractView from "./components/contract/ContractView";
import BonusesView from "./components/bonus/BonusesView";

function App() {
  return (
    <Router>
      <div>
        <Navigation />

        <Switch>
          <Route exact path="/">
            <Redirect to="/profile" />
          </Route>
          <Route path="/profile">
            <Profile />
          </Route>
          <Route path="/delegations">
            <DelegationsView />
          </Route>
          <Route path="/leaves">
            <LeavesView />
          </Route>
          <Route path="/contract">
            <ContractView />
          </Route>
          <Route path="/bonuses">
            <BonusesView />
          </Route>
        </Switch>
      </div>
    </Router>
  );
}

export default App;
