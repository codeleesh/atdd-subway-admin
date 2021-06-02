package nextstep.subway.section.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(List<Section> sections) {
        this.sections.addAll(sections);
    }

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();

        for (Section section : sections) {
            stations.addAll(
                    section.getStations()
            );
        }

        return stations;
    }
    public <R1> Stream<R1> mapOrderByUpStationToDownStation(Function<? super Section, ? extends R1> mapper) {
        List<Section> sorted = sort(sections.get(0));
        return sorted.stream()
                .map(mapper);
    }

    private List<Section> sort(Section of) {
        List<Section> sortedList = null;

        boolean findUpStation = false;
        for (Section section : sections) {
            if (of.getUpStation() == section.getDownStation()) {
                findUpStation = true;
                sortedList = sort(section);
                sortedList.add(of);
            }
        }

        if (!findUpStation) {
            sortedList = new ArrayList<>();
            sortedList.add(of);

            return sortedList;
        }

        return sortedList;
    }
}
