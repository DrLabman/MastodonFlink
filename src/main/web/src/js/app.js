import React, { Component } from "react";
import ReactDOM from "react-dom";
import { Navbar, Button, Card, Alignment, Elevation} from "@blueprintjs/core";
import { Flex, Box } from "reflexbox";

class Timeline extends Component {
    render() {
        return (
            <Box px={2} w={320}>
                <TimelineHeader/>
                <Article/>
            </Box>
        );
    }
}

class TimelineHeader extends Component {
    render() {
        return (
            <Navbar>
                <Navbar.Group align={Alignment.LEFT}>
                    <Navbar.Heading><Button className="bp3-minimal" icon="home" text="Home" />Timeline Title</Navbar.Heading>
                    <Navbar.Divider />
                </Navbar.Group>
                <Navbar.Group align={Alignment.RIGHT}>
                    <Button className="bp3-minimal" icon="settings"/>
                </Navbar.Group>
            </Navbar>
        );
    }
}

class Article extends Component {
    render() {
        return (
            <Card interactive={true} elevation={Elevation.TWO}>
                <h5>
                    <div style={{width: "48px", height: "48px", backgroundSize: "48px 48px", backgroundImage: "url(\"https://static.mastodon.technology/accounts/avatars/000/080/658/original/49b5b52e2b998dc0.png\")"}}></div>
                    <a href="https://elekk.xyz/@maloki/100651113717520579" target="_blank" rel="noopener">Card heading</a>
                    <a target="_blank" href="https://elekk.xyz/@maloki" title="maloki@elekk.xyz">
                        <span><bdi><strong>maloki</strong></bdi> <span>@maloki@elekk.xyz</span></span>
                    </a>
                    <time dateTime="2018-09-01T15:01:53.000Z" title="Sep 01, 2018, 16:01">5m</time>
                </h5>
                <div>
                    <p><span>Anxiety</span>
                        <Button text={"Show less"}/>
                    </p>
                    <div>
                        <p>Bodies are weird.. Like this generalized anxiety is so fucked most of the time...</p>
                        <p>I don't necessarily know where it's coming from, but all of a sudden my chest is hurting and my leg is jumping..</p>
                    </div>
                </div>
                <Button icon={"comment"}/>
                <Button icon={"share"}/>
                <Button icon={"star-empty"}/>
                <Button icon={"more"}/>
            </Card>
        );
    }
}

class FormContainer extends Component {
    constructor() {
        super();
        this.state = {
            title: ""
        };
    }
    render() {
        return (
            <Flex p={2} align="center">
                <Timeline></Timeline>
                <Box px={2} w={1/2}>Box B</Box>
            </Flex>
        );
    }
}

setTimeout(() => {
    ReactDOM.render(<FormContainer />, document.getElementById("react-root"));
}, 10);


