package jyad.link;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
public class LinkController {

    final MyLinkService linkService;

    public LinkController(MyLinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/link/{shortLink}")
    public ResponseEntity<?> getLink(@PathVariable String shortLink) {
        Link link = linkService.findLinkByShortLink(shortLink);
        if (link != null) {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(link.getOriginal())).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping("/link/post")
    public ResponseEntity<?> postLink(@RequestParam("link") String originalLink) {
        Link link = linkService.saveLink(originalLink);
        return ResponseEntity.ok(link);
    }

}
