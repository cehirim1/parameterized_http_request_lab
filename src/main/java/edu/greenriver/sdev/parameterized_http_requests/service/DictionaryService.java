package edu.greenriver.sdev.parameterized_http_requests.service;

import edu.greenriver.sdev.parameterized_http_requests.dtos.WordDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Slf4j
@Service
public class DictionaryService
{
    private Map<String, String> dictionary;

    public DictionaryService() throws FileNotFoundException
    {
        dictionary = new TreeMap<>();

        File file = ResourceUtils.getFile("classpath:dictionary.txt");
        Scanner reader = new Scanner(new FileInputStream(file));
        while (reader.hasNextLine())
        {
            String[] parts = reader.nextLine().split(": ");
            if (!parts[0].isEmpty() && !parts[1].isEmpty())
            {
                dictionary.put(parts[0].toLowerCase(), parts[1]);
            }
        }

        log.info("Loaded {} words from dictionary.txt", dictionary.size());
    }

    public List<String> allWords()
    {
        return new ArrayList<>(dictionary.keySet());
    }

    public Optional<String> getDefinition(String word)
    {
        return Optional.ofNullable(dictionary.get(word));
    }

    public WordDefinition addWord(String word, String definition)
    {
        word = word.trim().toLowerCase();
        if (!dictionary.containsKey(word))
        {
            dictionary.put(word, definition);
            return new WordDefinition(word, definition);
        }
        return null;
    }

    public WordDefinition addWord(WordDefinition pair)
    {
        return addWord(pair.getWord(), pair.getDefinition());
    }
}
