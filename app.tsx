
import { useMemo, useState } from "react";

type QuestDifficulty = "Easy" | "Medium" | "Hard";

type Quest = {
  id: number;
  title: string;
  description: string;
  tags: string[];
  difficulty: QuestDifficulty;
  gold: number;
};

type IconName =
  | "arrow"
  | "check"
  | "coin"
  | "garden"
  | "palette"
  | "plus"
  | "quest"
  | "shop"
  | "sparkle";

function Icon({
  name,
  size = 20,
  strokeWidth = 1.8,
}: {
  name: IconName;
  size?: number;
  strokeWidth?: number;
}) {
  const paths: Record<IconName, React.ReactNode> = {
    arrow: <path d="m9 5 7 7-7 7" />,
    check: <path d="m5 12 4 4L19 6" />,
    coin: (
      <>
        <circle cx="12" cy="12" r="8.5" />
        <path d="M9.5 9.5c.7-1.2 4.6-1.2 4.8.6.2 2-4.8 1.1-4.6 3.6.2 1.8 4 1.8 4.9.7M12 7.4v9.2" />
      </>
    ),
    garden: (
      <>
        <path d="M12 21V10" />
        <path d="M12 14c-4.4.1-6.3-2-6.5-5.8 4.2-.3 6.4 1.7 6.5 5.8ZM12 10c.2-4.2 2.4-6.2 6.5-5.8-.2 3.8-2.1 5.9-6.5 5.8Z" />
        <path d="M7 21h10" />
      </>
    ),
    palette: (
      <>
        <path d="M12 3a9 9 0 1 0 0 18h1.2a1.8 1.8 0 0 0 1.1-3.2 1.8 1.8 0 0 1 1.1-3.2H18A3 3 0 0 0 21 12a9 9 0 0 0-9-9Z" />
        <path d="M7.5 10h.01M9.5 6.8h.01M14 6.5h.01M17.2 9h.01" />
      </>
    ),
    plus: <path d="M12 5v14M5 12h14" />,
    quest: (
      <>
        <path d="M6.5 3.5h11v17h-11z" />
        <path d="M9.5 8h5M9.5 12h5M9.5 16h3" />
      </>
    ),
    shop: (
      <>
        <path d="M4 10h16l-1-5H5l-1 5ZM6 10v9h12v-9" />
        <path d="M9 19v-5h6v5M4 10c0 2 3.5 2 4 0 .5 2 3.5 2 4 0 .5 2 3.5 2 4 0 .5 2 4 2 4 0" />
      </>
    ),
    sparkle: (
      <>
        <path d="M12 2.5c.6 4.9 2.6 7 7.5 7.5-4.9.6-6.9 2.6-7.5 7.5-.6-4.9-2.6-6.9-7.5-7.5 4.9-.5 6.9-2.6 7.5-7.5Z" />
        <path d="M19 16.5c.2 1.8.9 2.5 2.5 2.5-1.6.2-2.3.9-2.5 2.5-.2-1.6-.9-2.3-2.5-2.5 1.6-.2 2.3-.9 2.5-2.5Z" />
      </>
    ),
  };

  return (
    <svg
      aria-hidden="true"
      fill="none"
      height={size}
      viewBox="0 0 24 24"
      width={size}
      stroke="currentColor"
      strokeLinecap="round"
      strokeLinejoin="round"
      strokeWidth={strokeWidth}
    >
      {paths[name]}
    </svg>
  );
}

const habits = [
  { id: 1, name: "Morning water", detail: "7 day streak", plant: "monstera", reward: 6 },
  { id: 2, name: "Read 20 mins", detail: "4 day streak", plant: "fern", reward: 8 },
  { id: 3, name: "Evening walk", detail: "Needs some care", plant: "sprout", reward: 10 },
];

const initialQuests: Quest[] = [
  {
    id: 1,
    title: "Book dentist appointment",
    description: "Find a nearby clinic and choose a time that works.",
    tags: ["Life admin", "Health"],
    difficulty: "Medium",
    gold: 35,
  },
  {
    id: 2,
    title: "Organize the photo drawer",
    description: "Sort loose prints into keep, share, and archive piles.",
    tags: ["Home"],
    difficulty: "Medium",
    gold: 25,
  },
  {
    id: 3,
    title: "Try a new soup recipe",
    description: "Pick something seasonal and make enough for tomorrow.",
    tags: ["Someday", "Cooking"],
    difficulty: "Easy",
    gold: 20,
  },
  {
    id: 4,
    title: "Send the postcards",
    description: "Write a quick note and drop them in the mailbox.",
    tags: ["Friends"],
    difficulty: "Easy",
    gold: 15,
  },
];

const difficultyRewards: Record<QuestDifficulty, number> = {
  Easy: 15,
  Medium: 25,
  Hard: 40,
};

const shopItems = [
  { id: 1, name: "Tiny mushroom", price: 40, art: "mushroom" },
  { id: 2, name: "Lucky snail", price: 65, art: "snail" },
  { id: 3, name: "Golden star", price: 90, art: "star" },
];

const plantShopItems = [
  { type: "bloom", name: "Coral blossom", price: 70 },
  { type: "cactus", name: "Desert cactus", price: 85 },
  { type: "lavender", name: "Soft lavender", price: 105 },
];

const backgroundShopItems = [
  { type: "classic", name: "Classic garden", price: 0 },
  { type: "sunroom", name: "Warm sunroom", price: 90 },
  { type: "evening", name: "Quiet evening", price: 120 },
  { type: "forest", name: "Forest nook", price: 150 },
];

function Plant({ type, done }: { type: string; done: boolean }) {
  return (
    <div className={`plant plant-${type} ${done ? "is-thriving" : ""}`} aria-hidden="true">
      <div className="plant-glow" />
      <div className="leaf leaf-1" />
      <div className="leaf leaf-2" />
      <div className="leaf leaf-3" />
      <div className="leaf leaf-4" />
      <div className="stem" />
      <div className="soil" />
      <div className="pot">
        <span />
      </div>
    </div>
  );
}

function Trinket({ type }: { type: string }) {
  if (type === "mushroom") {
    return (
      <div className="trinket mushroom">
        <span className="cap" />
        <span className="stalk" />
      </div>
    );
  }
  if (type === "snail") {
    return (
      <div className="trinket snail">
        <span className="shell" />
        <span className="body" />
        <span className="eye" />
      </div>
    );
  }
  return <div className="trinket star">✦</div>;
}

export default function App() {
  const [activeTab, setActiveTab] = useState<"garden" | "quests" | "shop">("garden");
  const [completedHabits, setCompletedHabits] = useState<number[]>([1]);
  const [completedQuests, setCompletedQuests] = useState<number[]>([]);
  const [quests, setQuests] = useState<Quest[]>(initialQuests);
  const [gold, setGold] = useState(120);
  const [owned, setOwned] = useState<number[]>([]);
  const [showAdd, setShowAdd] = useState(false);
  const [questTitle, setQuestTitle] = useState("");
  const [questDescription, setQuestDescription] = useState("");
  const [questTags, setQuestTags] = useState("");
  const [questDifficulty, setQuestDifficulty] = useState<QuestDifficulty>("Easy");
  const [plantTypes, setPlantTypes] = useState<Record<number, string>>({
    1: "monstera",
    2: "fern",
    3: "sprout",
  });
  const [editingPlant, setEditingPlant] = useState<number | null>(null);
  const [ownedPlantTypes, setOwnedPlantTypes] = useState<string[]>(["monstera", "fern", "sprout"]);
  const [ownedBackgrounds, setOwnedBackgrounds] = useState<string[]>(["classic"]);
  const [activeBackground, setActiveBackground] = useState("classic");

  const progress = useMemo(
    () => Math.round((completedHabits.length / habits.length) * 100),
    [completedHabits],
  );

  const toggleHabit = (id: number, reward: number) => {
    setCompletedHabits((current) => {
      const done = current.includes(id);
      setGold((value) => Math.max(0, value + (done ? -reward : reward)));
      return done ? current.filter((item) => item !== id) : [...current, id];
    });
  };

  const completeQuest = (id: number, reward: number) => {
    if (completedQuests.includes(id)) return;
    setCompletedQuests((current) => [...current, id]);
    setGold((value) => value + reward);
  };

  const addQuest = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const title = questTitle.trim();
    if (!title) return;

    const tags = questTags
      .split(",")
      .map((tag) => tag.trim())
      .filter(Boolean)
      .slice(0, 3);

    setQuests((current) => [
      ...current,
      {
        id: Math.max(0, ...current.map((quest) => quest.id)) + 1,
        title,
        description: questDescription.trim(),
        tags: tags.length ? tags : ["General"],
        difficulty: questDifficulty,
        gold: difficultyRewards[questDifficulty],
      },
    ]);
    setQuestTitle("");
    setQuestDescription("");
    setQuestTags("");
    setQuestDifficulty("Easy");
    setShowAdd(false);
  };

  const buyItem = (id: number, price: number) => {
    if (gold < price || owned.includes(id)) return;
    setGold((value) => value - price);
    setOwned((current) => [...current, id]);
  };

  const buyPlantType = (type: string, price: number) => {
    if (gold < price || ownedPlantTypes.includes(type)) return;
    setGold((value) => value - price);
    setOwnedPlantTypes((current) => [...current, type]);
  };

  const buyBackground = (type: string, price: number) => {
    if (ownedBackgrounds.includes(type)) {
      setActiveBackground(type);
      return;
    }
    if (gold < price) return;
    setGold((value) => value - price);
    setOwnedBackgrounds((current) => [...current, type]);
    setActiveBackground(type);
  };

  return (
    <main className="app-shell">
      <div className="ambient ambient-one" />
      <div className="ambient ambient-two" />

      <section className="phone-frame">
        <header className="topbar">
          <div>
            <p className="eyebrow">
              {activeTab === "garden" ? "Tuesday, 14 May" : "Your little world"}
            </p>
            <h1>
              {activeTab === "garden"
                ? "Good morning, Mia"
                : activeTab === "quests"
                  ? "Quests"
                  : "Trinket shop"}
            </h1>
          </div>
          <div className="gold-pill" aria-label={`${gold} gold`}>
            <Icon name="coin" size={17} />
            <strong>{gold}</strong>
          </div>
        </header>

        <div className="screen-content">
          {activeTab === "garden" && (
            <section className="shelf-screen">
              <div className="shelf-screen-heading">
                <div className="garden-heading">
                  <div>
                    <span className="section-kicker">TODAY&apos;S GARDEN</span>
                    <h2>{completedHabits.length === habits.length ? "Everything is blooming!" : "Your growing collection"}</h2>
                  </div>
                  <div className="progress-ring" style={{ "--progress": `${progress * 3.6}deg` } as React.CSSProperties}>
                    <span>{completedHabits.length}/{habits.length}</span>
                  </div>
                </div>
                <p>Care for each plant, or make it feel more like yours.</p>
              </div>

              <div className={`tall-shelf garden-theme-${activeBackground}`}>
                <div className="shelf-window">
                  <span className="sun" />
                  <span className="cloud cloud-one" />
                  <span className="cloud cloud-two" />
                </div>
                {habits.map((habit, index) => {
                  const done = completedHabits.includes(habit.id);
                  return (
                    <div className={`shelf-level shelf-level-${index + 1}`} key={habit.id}>
                      <div className="shelf-plant-wrap">
                        <Plant type={plantTypes[habit.id]} done={done} />
                        <button
                          className="customize-plant"
                          onClick={() => setEditingPlant(habit.id)}
                          aria-label={`Change plant for ${habit.name}`}
                        >
                          <Icon name="palette" size={15} />
                        </button>
                      </div>
                      <div className="shelf-habit-card">
                        <div>
                          <small>{done ? "THRIVING TODAY" : habit.detail.toUpperCase()}</small>
                          <strong>{habit.name}</strong>
                        </div>
                        <button
                          className={`shelf-check ${done ? "done" : ""}`}
                          onClick={() => toggleHabit(habit.id, habit.reward)}
                          aria-label={`Mark ${habit.name} ${done ? "incomplete" : "complete"}`}
                        >
                          <Icon name="check" size={17} strokeWidth={2.5} />
                        </button>
                      </div>
                      <div className="shelf-plank">
                        {index === 1 && owned.slice(0, 2).map((itemId) => {
                          const item = shopItems.find((shopItem) => shopItem.id === itemId);
                          return item ? <Trinket key={item.id} type={item.art} /> : null;
                        })}
                      </div>
                    </div>
                  );
                })}
                <div className="shelf-post post-left" />
                <div className="shelf-post post-right" />
                <span className="floor-shadow" />
              </div>

              <div className="shelf-actions">
                <button onClick={() => setShowAdd(true)}>
                  <Icon name="plus" size={18} />
                  Add a habit
                </button>
                <button onClick={() => setActiveTab("shop")}>
                  <Icon name="shop" size={18} />
                  Find trinkets
                </button>
              </div>
            </section>
          )}

          {activeTab === "quests" && (
            <section className="page-panel">
              <div className="quest-hero">
                <div>
                  <span className="section-kicker">BACKLOG</span>
                  <h2>Small adventures,<br />whenever you&apos;re ready.</h2>
                  <p>No deadlines here. Pick a sidequest and earn a little gold.</p>
                </div>
                <div className="map-art">
                  <span className="path" />
                  <span className="map-pin pin-one" />
                  <span className="map-pin pin-two" />
                  <span className="flag" />
                </div>
              </div>
              <div className="quest-list">
                {quests.map((quest) => {
                  const done = completedQuests.includes(quest.id);
                  return (
                    <button
                      className={`quest-row ${done ? "done" : ""}`}
                      key={quest.id}
                      onClick={() => completeQuest(quest.id, quest.gold)}
                    >
                      <span className="quest-status">{done && <Icon name="check" size={16} />}</span>
                      <span className="quest-copy">
                        <span className="quest-meta">
                          <small className={`difficulty difficulty-${quest.difficulty.toLowerCase()}`}>
                            {quest.difficulty}
                          </small>
                          {quest.tags.map((tag) => <small key={tag}>{tag}</small>)}
                        </span>
                        <strong>{quest.title}</strong>
                        {quest.description && <span className="quest-description">{quest.description}</span>}
                      </span>
                      <span className="quest-gold">
                        <Icon name="coin" size={15} />
                        {done ? "Done" : quest.gold}
                      </span>
                    </button>
                  );
                })}
              </div>
              <button className="primary-button" onClick={() => setShowAdd(true)}>
                <Icon name="plus" size={19} /> Add a sidequest
              </button>
            </section>
          )}

          {activeTab === "shop" && (
            <section className="page-panel">
              <div className="shop-hero">
                <span className="shop-sparkle sparkle-left">✦</span>
                <div className="display-plinth">
                  <Trinket type="mushroom" />
                  <Trinket type="snail" />
                </div>
                <span className="shop-sparkle sparkle-right">✦</span>
              </div>
              <div className="shop-intro">
                <span className="section-kicker">MAKE IT YOURS</span>
                <h2>Find something lovely</h2>
                <p>Spend the gold you earn on tiny treasures for your garden shelf.</p>
              </div>
              <div className="shop-section-heading">
                <div>
                  <span className="section-kicker">FOR YOUR SHELF</span>
                  <h3>Tiny trinkets</h3>
                </div>
                <span>{owned.length}/{shopItems.length}</span>
              </div>
              <div className="shop-grid">
                {shopItems.map((item) => {
                  const isOwned = owned.includes(item.id);
                  const canAfford = gold >= item.price;
                  return (
                    <button
                      className={`shop-item ${isOwned ? "owned" : ""}`}
                      key={item.id}
                      onClick={() => buyItem(item.id, item.price)}
                      disabled={isOwned || !canAfford}
                    >
                      <span className="item-art"><Trinket type={item.art} /></span>
                      <strong>{item.name}</strong>
                      <span className="item-price">
                        {isOwned ? (
                          <>On shelf <Icon name="check" size={14} /></>
                        ) : (
                          <><Icon name="coin" size={14} /> {item.price}</>
                        )}
                      </span>
                    </button>
                  );
                })}
              </div>

              <div className="shop-section-heading">
                <div>
                  <span className="section-kicker">NEW VARIETIES</span>
                  <h3>Plants to grow</h3>
                </div>
                <span>{ownedPlantTypes.length - 3}/{plantShopItems.length}</span>
              </div>
              <div className="plant-shop-list">
                {plantShopItems.map((item) => {
                  const isOwned = ownedPlantTypes.includes(item.type);
                  return (
                    <button
                      className={`plant-shop-item ${isOwned ? "owned" : ""}`}
                      key={item.type}
                      onClick={() => buyPlantType(item.type, item.price)}
                      disabled={!isOwned && gold < item.price}
                    >
                      <span className="shop-plant-art">
                        <Plant type={item.type} done />
                      </span>
                      <span className="shop-product-copy">
                        <small>{isOwned ? "IN YOUR COLLECTION" : "PLANT VARIETY"}</small>
                        <strong>{item.name}</strong>
                        <em>Use this style for any habit</em>
                      </span>
                      <span className="shop-buy-pill">
                        {isOwned ? (
                          <><Icon name="check" size={14} /> Owned</>
                        ) : (
                          <><Icon name="coin" size={14} /> {item.price}</>
                        )}
                      </span>
                    </button>
                  );
                })}
              </div>

              <div className="shop-section-heading">
                <div>
                  <span className="section-kicker">SET THE MOOD</span>
                  <h3>Garden backgrounds</h3>
                </div>
                  <span>{ownedBackgrounds.length}/{backgroundShopItems.length}</span>
              </div>
              <div className="background-shop-list">
                {backgroundShopItems.map((item) => {
                  const isOwned = ownedBackgrounds.includes(item.type);
                  const isActive = activeBackground === item.type;
                  return (
                    <button
                      className={`background-item theme-preview-${item.type} ${isActive ? "active" : ""}`}
                      key={item.type}
                      onClick={() => buyBackground(item.type, item.price)}
                      disabled={!isOwned && gold < item.price}
                    >
                      <span className="background-preview">
                        <i className="preview-window" />
                        <i className="preview-shelf one" />
                        <i className="preview-shelf two" />
                        <i className="preview-plant" />
                      </span>
                      <span className="background-info">
                        <strong>{item.name}</strong>
                        <small>
                          {isActive ? "Using now" : isOwned ? "Tap to use" : <><Icon name="coin" size={13} /> {item.price}</>}
                        </small>
                      </span>
                      {isActive && <span className="active-theme-check"><Icon name="check" size={14} /></span>}
                    </button>
                  );
                })}
              </div>
              <div className="shop-note">
                <Icon name="sparkle" size={18} />
                New treasures arrive every Sunday
              </div>
            </section>
          )}
        </div>

        <nav className="bottom-nav" aria-label="Main navigation">
          {[
            { id: "garden", label: "Garden", icon: "garden" },
            { id: "quests", label: "Quests", icon: "quest" },
            { id: "shop", label: "Shop", icon: "shop" },
          ].map((item) => (
            <button
              className={activeTab === item.id ? "active" : ""}
              key={item.id}
              onClick={() => setActiveTab(item.id as typeof activeTab)}
            >
              <span><Icon name={item.icon as IconName} size={22} /></span>
              {item.label}
            </button>
          ))}
        </nav>

        {showAdd && (
          <div className="modal-backdrop" onClick={() => setShowAdd(false)}>
            <div className="bottom-sheet" onClick={(event) => event.stopPropagation()}>
              <span className="sheet-handle" />
              {activeTab === "quests" ? (
                <form className="quest-form" onSubmit={addQuest}>
                  <span className="sheet-icon"><Icon name="quest" size={25} /></span>
                  <h2>Add a new quest</h2>
                  <p>Give your adventure a little shape before you begin.</p>
                  <label>
                    Quest name
                    <input
                      autoFocus
                      maxLength={60}
                      onChange={(event) => setQuestTitle(event.target.value)}
                      placeholder="What would you like to do?"
                      required
                      value={questTitle}
                    />
                  </label>
                  <label>
                    Description
                    <textarea
                      maxLength={140}
                      onChange={(event) => setQuestDescription(event.target.value)}
                      placeholder="Add a few helpful details..."
                      rows={3}
                      value={questDescription}
                    />
                  </label>
                  <label>
                    Tags
                    <input
                      maxLength={60}
                      onChange={(event) => setQuestTags(event.target.value)}
                      placeholder="Home, errands, someday"
                      value={questTags}
                    />
                    <small>Separate up to three tags with commas.</small>
                  </label>
                  <fieldset>
                    <legend>Difficulty</legend>
                    <div className="difficulty-options">
                      {(Object.keys(difficultyRewards) as QuestDifficulty[]).map((difficulty) => (
                        <button
                          className={questDifficulty === difficulty ? "selected" : ""}
                          key={difficulty}
                          onClick={() => setQuestDifficulty(difficulty)}
                          type="button"
                        >
                          <strong>{difficulty}</strong>
                          <span><Icon name="coin" size={13} /> {difficultyRewards[difficulty]}</span>
                        </button>
                      ))}
                    </div>
                  </fieldset>
                  <button className="primary-button" type="submit">Add to quests</button>
                  <button className="text-button" onClick={() => setShowAdd(false)} type="button">Maybe later</button>
                </form>
              ) : (
                <>
                  <span className="sheet-icon"><Icon name="garden" size={25} /></span>
                  <h2>Plant something new</h2>
                  <p>Your next habit or sidequest can grow here.</p>
                  <input aria-label="Task name" autoFocus placeholder="What would you like to do?" />
                  <button className="primary-button" onClick={() => setShowAdd(false)}>Add to my garden</button>
                  <button className="text-button" onClick={() => setShowAdd(false)}>Maybe later</button>
                </>
              )}
            </div>
          </div>
        )}

        {editingPlant !== null && (
          <div className="modal-backdrop" onClick={() => setEditingPlant(null)}>
            <div className="bottom-sheet plant-picker" onClick={(event) => event.stopPropagation()}>
              <span className="sheet-handle" />
              <span className="section-kicker">CHOOSE A PLANT</span>
              <h2>What should grow here?</h2>
              <p>You can change your plant whenever you like.</p>
              <div className="plant-options">
                {[
                  { type: "monstera", label: "Monstera" },
                  { type: "fern", label: "Fern" },
                  { type: "sprout", label: "Sprout" },
                  { type: "bloom", label: "Blossom" },
                  { type: "cactus", label: "Cactus" },
                  { type: "lavender", label: "Lavender" },
                ].filter((option) => ownedPlantTypes.includes(option.type)).map((option) => (
                  <button
                    className={plantTypes[editingPlant] === option.type ? "selected" : ""}
                    key={option.type}
                    onClick={() => {
                      setPlantTypes((current) => ({ ...current, [editingPlant]: option.type }));
                      setEditingPlant(null);
                    }}
                  >
                    <span><Plant type={option.type} done /></span>
                    <strong>{option.label}</strong>
                    <i><Icon name="check" size={13} /></i>
                  </button>
                ))}
              </div>
              <button
                className="picker-shop-link"
                onClick={() => {
                  setEditingPlant(null);
                  setActiveTab("shop");
                }}
              >
                <Icon name="shop" size={17} />
                Find more plants in the shop
              </button>
              <button className="text-button" onClick={() => setEditingPlant(null)}>Cancel</button>
            </div>
          </div>
        )}
      </section>
    </main>
  );
}
