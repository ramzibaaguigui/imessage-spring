package jyad.link;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyLinkService {

    @Autowired
    LinkRepository linkRepository;


    public Link findLinkByShortLink(String shortLink) {
        Optional<Link> link = linkRepository.findAll()
                .stream()
                .filter(aLink -> aLink.getShortened().equals(shortLink))
                .findFirst();

        return link.orElse(null);
    }

    public Link saveLink(String originalLink) {
        int shortLinkLength = getShortLinkLength();
        Link link = new Link();
        link.setOriginal(originalLink);
        link.setShortened(RandomString.make(shortLinkLength));
        link = linkRepository.save(link);
        return link;
    }

    private int getShortLinkLength() {
        return 8;
    }
}
